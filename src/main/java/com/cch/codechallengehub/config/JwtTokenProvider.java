package com.cch.codechallengehub.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {

	@Value("${spring.application.name}")
	private String issuer;

	private final SecretKey secretKey;

	private final Long expireMilliseconds;

	public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey
			, @Value("${jwt.expiration}") Long expireMilliseconds) {
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
		this.expireMilliseconds = expireMilliseconds;
	}

	public String createToken(String email) {
		Instant now = Instant.now();
		Instant expired = now.plusMillis(expireMilliseconds);

		Date issuedAt = Date.from(now);
		Date expiration = Date.from(expired);

		return Jwts.builder()
			.subject(email)
			.issuer(issuer)
			.issuedAt(issuedAt)
			.expiration(expiration)
			.signWith(secretKey, SIG.HS256)
			.compact();
	}

	public boolean verifyToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token);
			return true;
		} catch (ExpiredJwtException eje) {
			log.warn("token expire " + eje.getMessage());
		} catch (Exception e) {
			log.error(token + " " + e.getMessage());
		}
		return false;
	}

	public String getEmailFromToken(String token) {
		Claims payload = Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();

		return payload.getSubject();
	}

}
