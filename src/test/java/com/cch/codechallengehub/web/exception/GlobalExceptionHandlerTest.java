package com.cch.codechallengehub.web.exception;

import static com.cch.codechallengehub.web.exception.ErrorCode.UNKNOWN_ERROR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(TestController.class)
class GlobalExceptionHandlerTest {

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
	@DisplayName("Exception Test")
	void exception_test() throws Exception {
	    // given
		MockHttpServletRequestBuilder builder = get("/exception");
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		ErrorResponse<ErrorCode> errorResponse = ErrorResponse.<ErrorCode>builder()
			.errorCode(UNKNOWN_ERROR)
			.msg("Exception!")
			.build();
		String expectBody = objectMapper.writeValueAsString(errorResponse);
		perform.andExpect(status().isInternalServerError())
			.andExpect(content().string(expectBody));
	}

	@Test
	@DisplayName("Runtime Exception Test")
	void runtime_exception_test() throws Exception {
		// given
		MockHttpServletRequestBuilder builder = get("/runtime-exception");
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		ErrorResponse<ErrorCode> errorResponse = ErrorResponse.<ErrorCode>builder()
			.errorCode(UNKNOWN_ERROR)
			.msg("RuntimeException!")
			.build();
		String expectBody = objectMapper.writeValueAsString(errorResponse);
		perform.andExpect(status().isInternalServerError())
			.andExpect(content().string(expectBody));
	}

}