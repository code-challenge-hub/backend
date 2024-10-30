package com.cch.codechallengehub.service;

import com.cch.codechallengehub.constants.AuthorizationType;
import com.cch.codechallengehub.constants.UserRole;
import com.cch.codechallengehub.domain.RefreshToken;
import com.cch.codechallengehub.domain.User;
import com.cch.codechallengehub.dto.UserDto;
import com.cch.codechallengehub.repository.UserRepository;
import com.cch.codechallengehub.token.util.JWTUtil;
import com.cch.codechallengehub.token.util.RefreshTokenUtil;
import com.cch.codechallengehub.util.CommonUtil;
import com.cch.codechallengehub.web.exception.AccessDeniedException;
import com.cch.codechallengehub.web.exception.BadRequestException;
import com.cch.codechallengehub.web.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final RefreshTokenUtil refreshTokenUtil;

    public void joinProcess(UserDto userDto) {

        String nickname = userDto.getNickname();
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {
            throw new BadRequestException("This email already exists.");
        }

        User data = User.builder()
                        .nickname(nickname)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .role(UserRole.USER.toString())
                        .build();

        userRepository.save(data);
    }

    public void reisuue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new BadRequestException("refresh token null");
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(AuthorizationType.REFRESH_TOKEN.name())) {
                refresh = cookie.getValue();
            }
        }
        if (refresh == null) {
            throw new BadRequestException("refresh token null");
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new AccessDeniedException("refresh token expired");
        }

        String category = jwtUtil.getCategory(refresh);

        if (!category.equals(AuthorizationType.REFRESH_TOKEN.name())) {
            throw new AccessDeniedException("refresh token expired");
        }
        String request_ip = CommonUtil.getRemoteIP(request);
        String ip = jwtUtil.getIp(refresh);
        String email = jwtUtil.getEmail(refresh);

        if(!request_ip.equals(ip)){
            refreshTokenUtil.deleteRefreshToken(email);
            throw new UnauthorizedException("Suspicious activity detected. Your token has been terminated for security reasons. Please log in again.");
        }

        String role = jwtUtil.getRole(refresh);

        //redis에 refresh token이 존재하고, cookie값과 같은지 확인
        //없다면 리프레쉬토큰 초기화 ( 로그아웃 )
        Optional<RefreshToken> redisToken = refreshTokenUtil.getRefreshToken(email);
        if(redisToken.isEmpty()){
            throw new AccessDeniedException("refresh token expired");
        }

        if(!redisToken.get().getRefreshToken().equals(refresh)){
            refreshTokenUtil.deleteRefreshToken(email);
            throw new UnauthorizedException("Suspicious activity detected. Your token has been terminated for security reasons. Please log in again.");
        }

        String newAccess = jwtUtil.createJwt(AuthorizationType.ACCESS_TOKEN.name(), email, role, request_ip);
        String newRefresh = jwtUtil.createJwt(AuthorizationType.REFRESH_TOKEN.name(), email, role, request_ip,14400000L);

        refreshTokenUtil.saveRefreshToken(email, newRefresh);

        response.addHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(refreshTokenUtil.createCookie(newRefresh));

    }
}
