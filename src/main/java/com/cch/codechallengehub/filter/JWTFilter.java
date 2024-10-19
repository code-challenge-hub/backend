package com.cch.codechallengehub.filter;

import com.cch.codechallengehub.constants.AuthorizationType;
import com.cch.codechallengehub.domain.User;
import com.cch.codechallengehub.dto.CustomUserDetails;
import com.cch.codechallengehub.token.util.JWTUtil;
import com.cch.codechallengehub.util.CommonUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final String permitUrl;

    public JWTFilter(JWTUtil jwtUtil, String permitUrl) {
        this.jwtUtil = jwtUtil;
        this.permitUrl = permitUrl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        if (requestUri.startsWith(permitUrl)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization= request.getHeader("Authorization");
        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(token);

        if (!category.equals(AuthorizationType.ACCESS_TOKEN.name())) {
            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");
            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String request_ip = CommonUtil.getRemoteIP(request);
        String ip = jwtUtil.getIp(token);

        if(!request_ip.equals(ip)){
            //response body
            PrintWriter writer = response.getWriter();
            writer.print("Suspicious activity detected. Your token has been terminated for security reasons. Please log in again.");
            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);

        User user = User.builder()
                        .email(email)
                        .role(role)
                        .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
