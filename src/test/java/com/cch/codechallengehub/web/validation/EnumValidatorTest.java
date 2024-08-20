package com.cch.codechallengehub.web.validation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cch.codechallengehub.web.exception.ErrorCode;
import com.cch.codechallengehub.web.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(EnumTestController.class)
class EnumValidatorTest {

	@Autowired
	WebApplicationContext context;

	MockMvc mockMvc;

	ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders
			.webAppContextSetup(context)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();
	}

	@Test
	void challenge_level_valid() throws Exception {
		// given
		MockHttpServletRequestBuilder requestBuilder = post("/parameter-challenge-level")
			.content("BEGINNER");
		// when
		ResultActions perform = mockMvc.perform(requestBuilder);

		// then
		perform.andExpect(status().isOk());
	}

	@Test
	void challenge_level_not_valid() throws Exception {
	    // given
		MockHttpServletRequestBuilder requestBuilder = post("/parameter-challenge-level")
			.content("NOT_LEVEL");
		// when
		ResultActions perform = mockMvc.perform(requestBuilder);

		// then
		ErrorResponse<ErrorCode> errorResponse = ErrorResponse.<ErrorCode>builder()
			.errorCode(ErrorCode.NOT_VALID_PARAM)
			.msg("challenge level is not valid")
			.build();
		String errorMsg = objectMapper.writeValueAsString(errorResponse);
		perform.andExpect(status().isBadRequest())
			.andExpect(content().string(errorMsg));
	}

}