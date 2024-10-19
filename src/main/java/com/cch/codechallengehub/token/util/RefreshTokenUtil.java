package com.cch.codechallengehub.token.util;

import com.cch.codechallengehub.constants.AuthorizationType;
import com.cch.codechallengehub.domain.RefreshToken;
import com.cch.codechallengehub.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenUtil {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(String email, String refreshToken){
        RefreshToken redis = new RefreshToken(email, refreshToken);
        refreshTokenRepository.save(redis);
    }

    public boolean existRefreshToken(String email){
        return refreshTokenRepository.existsById(email);
    }

    public void deleteRefreshToken(String email){
        refreshTokenRepository.deleteById(email);
    }

    public Optional<RefreshToken> getRefreshToken(String email){
        return refreshTokenRepository.findById(email);
    }

    public Cookie createCookie(String value) {
        Cookie cookie = new Cookie(AuthorizationType.REFRESH_TOKEN.name(), value);
        cookie.setMaxAge(5*60*60);
        cookie.setHttpOnly(true);
        //cookie.setPath("/");
        return cookie;
    }

    public Cookie cleCookie(){
        Cookie cookie = new Cookie(AuthorizationType.REFRESH_TOKEN.name(), null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }


}
