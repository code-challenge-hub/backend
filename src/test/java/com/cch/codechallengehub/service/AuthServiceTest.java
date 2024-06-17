package com.cch.codechallengehub.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.cch.codechallengehub.exception.NotAuthenticationException;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@ActiveProfiles("test")
@SpringJUnitConfig
@TestPropertySource(locations = "classpath:application-test.yml")
class AuthServiceTest {

	@Autowired
	Environment env;

	@Mock
	AuthenticationManager authenticationManager;

	@Mock
	JwtTokenProvider jwtTokenProvider;

	@InjectMocks
	AuthService authService;


	@BeforeEach
	void setUp() {
	}

	@Test
	void authenticate_success() {
	    // given
		String email = "gasfa@aafas.com";
		String password = "1234";
		String testToken = "asfsafasfsadsaldjaslkd";

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			email, password);
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authenticationToken);
		when(jwtTokenProvider.createToken(any())).thenReturn(testToken);

		// when
		String resultToken = authService.authenticate(email, password);

		// then
		assertThat(testToken).isEqualTo(resultToken);
	}

	@Test
	void authenticate_fail() {
	    // given
		String email = "gasfa@aafas.com";
		String password = "1234";
		String testToken = "asfsafasfsadsaldjaslkd";
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationException("not valid") {});
		when(jwtTokenProvider.createToken(any())).thenReturn(testToken);

		// when
		AbstractThrowableAssert<?, ? extends Throwable> thrownBy = assertThatThrownBy(
			() -> authService.authenticate(email, password));

		// then
		thrownBy.isInstanceOf(NotAuthenticationException.class);
	}
}