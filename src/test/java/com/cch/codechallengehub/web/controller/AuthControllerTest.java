package com.cch.codechallengehub.web.controller;

import static com.cch.codechallengehub.security.AuthorizationHeader.AUTHORIZATION;
import static com.cch.codechallengehub.security.AuthorizationHeader.BEARER;
import static com.cch.codechallengehub.web.exception.ErrorCode.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cch.codechallengehub.service.AuthService;
import com.cch.codechallengehub.service.JwtTokenProvider;
import com.cch.codechallengehub.web.dto.LoginRequest;
import com.cch.codechallengehub.web.exception.AuthenticationExceptionHandler;
import com.cch.codechallengehub.web.exception.ErrorCode;
import com.cch.codechallengehub.web.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

	MockMvc mockMvc;

	ObjectMapper objectMapper;

	@MockBean
	AuthService authService;

	@MockBean
	UserDetailsService userDetailsService;

	@MockBean
	JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders
			.standaloneSetup(new AuthController(authService))
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.setControllerAdvice(new AuthenticationExceptionHandler())
			.build();
		objectMapper = new ObjectMapper();
	}

	@Test
	void login_success_test() throws Exception {
		// given
		String loginUrl = "/api/v1/auth/login";
		String email = "test@test.com";
		String password = "asdf112";
		String token = "test-token";
		LoginRequest request = LoginRequest.builder()
			.email(email)
			.password(password)
			.build();
		String requestJson = objectMapper.writeValueAsString(request);

		when(authService.authenticate(email, password)).thenReturn(token);
		MockHttpServletRequestBuilder builder = post(loginUrl)
			.content(requestJson)
			.contentType(MediaType.APPLICATION_JSON);
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		perform.andExpect(status().isOk())
			.andExpect(header().string(AUTHORIZATION.getValue(), BEARER.getValue() + " " + token));
	}
	@Test
	void login_fail_test() throws Exception {
		// given
		String loginUrl = "/api/v1/auth/login";
		String email = "test@test.com";
		String password = "asdf112";

		LoginRequest request = LoginRequest.builder()
			.email(email)
			.password(password)
			.build();
		String requestJson = objectMapper.writeValueAsString(request);

		when(authService.authenticate(email, password)).thenThrow(new BadCredentialsException("Email, Password Not Valid"));
		MockHttpServletRequestBuilder builder = post(loginUrl)
			.content(requestJson)
			.contentType(MediaType.APPLICATION_JSON);
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		ErrorResponse<ErrorCode> errorResponse = ErrorResponse.<ErrorCode>builder()
			.errorCode(NO_AUTHENTICATION)
			.msg("Email, Password Not Valid")
			.build();
		String errorJson = objectMapper.writeValueAsString(errorResponse);
		perform.andExpect(status().isUnauthorized())
			.andExpect(content().string(errorJson));

	}
}