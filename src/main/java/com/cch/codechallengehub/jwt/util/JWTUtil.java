package com.cch.codechallengehub.jwt.util;

import com.cch.codechallengehub.constants.JwtKey;
import com.cch.codechallengehub.jwt.adapter.JwtKeyLocator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Locator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JWTUtil {

    private final long jwtExpiration;

    public JWTUtil(@Value("${jwt.expiration-time}")long jwtExpiration) {
        this.jwtExpiration = jwtExpiration;
    }

    public String createJwt(String username, String role) {
        String keyId = JwtKey.SECRET.getKeyId();
        return Jwts.builder()
                .header().keyId(keyId).and()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(JwtKeyLocator.getKey(keyId))
                .compact();
    }

    private Claims extractAllClaims(String token) {
        Locator<Key> keyLocator = getJwtKeyLocator();
        return Jwts
                .parser()
                .keyLocator(keyLocator)
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

    public Boolean isExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Locator<Key> getJwtKeyLocator() {
        return new JwtKeyLocator();
    }
}
