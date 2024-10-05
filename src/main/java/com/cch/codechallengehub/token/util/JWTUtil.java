package com.cch.codechallengehub.token.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JWTUtil {

    private final SecretKey secretKey;
    private final long jwtExpiration;

    public JWTUtil(@Value("${jwt.secret-key}")String secret, @Value("${jwt.expiration-time}")long jwtExpiration) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.jwtExpiration = jwtExpiration;
    }

    public String createJwt(String category, String username, String role, String ip) {
        return createJwt(category, username, role, ip, jwtExpiration);
    }

    public String createJwt(String category, String username, String role, String ip, Long expiredMs) {
        return Jwts.builder()
                .subject(username)
                .claim("category", category)
                .claim("role", role)
                .claim("ip", ip)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String getEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String getRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public String getIp(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("ip", String.class);
    }

    public Boolean isExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

}
