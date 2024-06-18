package com.cch.codechallengehub.security;

import static com.cch.codechallengehub.security.AuthorizationHeader.AUTHORIZATION;
import static com.cch.codechallengehub.security.AuthorizationHeader.BEARER;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cch.codechallengehub.config.SecurityConfig;
import com.cch.codechallengehub.service.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest(AuthenticationTestController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtAuthenticationEntryPoint.class})
class JwtAuthenticationFilterTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	UserDetailsService userDetailsService;

	@MockBean
	JwtTokenProvider jwtTokenProvider;

	@Test
	void permit_all_url_test() throws Exception {
	    // given
		String permitAllUrl = "/api/v1/auth/login";
		MockHttpServletRequestBuilder builder = post(permitAllUrl);
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		perform.andExpect(status().isOk());
	}

	@Test
	void fail_authentication_test() throws Exception {
	    // given
		String needAuth = "/need/authentication";
		String token = "asdasdasd";
		when(jwtTokenProvider.verifyToken(token)).thenReturn(false);
		MockHttpServletRequestBuilder builder = get(needAuth);
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		perform.andExpect(status().isUnauthorized());
	}
	@Test
	void success_authentication_test() throws Exception {
	    // given
		String needAuth = "/need/authentication";
		String token = "test-token";
		String email = "test@user.com";
		String bearerToken = BEARER.getValue() + " " + token;
		when(jwtTokenProvider.verifyToken(token)).thenReturn(true);
		when(jwtTokenProvider.getEmailFromToken(token)).thenReturn(email);
		when(userDetailsService.loadUserByUsername(email)).thenReturn(createTestUserDetails(email));
		MockHttpServletRequestBuilder builder = get(needAuth).header(AUTHORIZATION.getValue(), bearerToken);
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		perform.andExpect(status().isOk());
	}

	private UserDetails createTestUserDetails(String email) {
		return User.builder()
			.username(email)
			.password("2134")
			.authorities("USER")
			.build();
	}

}