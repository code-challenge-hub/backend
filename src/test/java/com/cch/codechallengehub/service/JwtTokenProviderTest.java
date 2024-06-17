package com.cch.codechallengehub.service;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@ActiveProfiles("test")
@SpringJUnitConfig
@TestPropertySource(locations = "classpath:application-test.yml")
class JwtTokenProviderTest {

	@Autowired
	Environment env;
	JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	void setUp() {
		String secretKey = env.getProperty("secret-key");
		Long expiration = Long.parseLong(requireNonNull(env.getProperty("expiration")));
		jwtTokenProvider = new JwtTokenProvider(secretKey, expiration);
	}

	@Test
	void createTokenTest() {
	    // given
	    String email = "test@nagd.com";
	    // when
		String token = jwtTokenProvider.createToken(email);
		// then
		assertThat(token).isNotBlank();
	}
	
	@Test
	void verifyTokenTest() {
		// given
		String email = "test@nagd.com";
		String token = jwtTokenProvider.createToken(email);
		// when
		boolean valid = jwtTokenProvider.verifyToken(token);
		// then
		assertThat(valid).isTrue();
	}
	@Test
	void notVerifyTokenTest() {
		// given
		String email = "test@nagd.com";
		String token = jwtTokenProvider.createToken(email);
		token = token + "2";
		// when
		boolean valid = jwtTokenProvider.verifyToken(token);
		// then
		assertThat(valid).isFalse();
	}

	@Test
	void getEmailFromTokenTest() {
		// given
		String email = "test@nagd.com";
		String token = jwtTokenProvider.createToken(email);
		// when
		String emailFromToken = jwtTokenProvider.getEmailFromToken(token);
		// then
		assertThat(email).isEqualTo(emailFromToken);
	}

}