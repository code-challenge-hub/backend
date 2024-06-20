package com.cch.codechallengehub.web.exception;

import static com.cch.codechallengehub.web.exception.ErrorCode.NOT_VALID_PARAM;
import static com.cch.codechallengehub.web.exception.ErrorCode.NO_FOUND_RESOURCE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(TestController.class)
class MvcExceptionHandlerTest {

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
	@DisplayName("Missing Servlet Request Parameter Test")
	void handle_missing_servlet_request_parameter_test() throws Exception {
		// given
		MockHttpServletRequestBuilder builder = get("/handle-missing-servlet-request-parameter");
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		ErrorResponse<ErrorCode> errorResponse = ErrorResponse.<ErrorCode>builder()
			.errorCode(NOT_VALID_PARAM)
			.msg("Required request parameter 'num' for method parameter type int is not present")
			.build();
		String expectBody = objectMapper.writeValueAsString(errorResponse);
		perform.andExpect(status().isBadRequest())
			.andExpect(content().string(expectBody));
	}

	@Test
	@DisplayName("No Resource Found Exception Test")
	void no_resource_found_exception_test() throws Exception {
		// given
		MockHttpServletRequestBuilder builder = get("/no-resource-exception");
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		ErrorResponse<ErrorCode> errorResponse = ErrorResponse.<ErrorCode>builder()
			.errorCode(NO_FOUND_RESOURCE)
			.msg("No Found Resource [GET] /no-resource-exception")
			.build();
		String expectBody = objectMapper.writeValueAsString(errorResponse);
		perform.andExpect(status().isNotFound())
			.andExpect(content().string(expectBody));
	}

	@Test
	@DisplayName("Method Argument Not Valid Exception Test")
	void method_argument_not_valid_exception_test() throws Exception {
		// given
		TestDto testDto = new TestDto();
		testDto.setId(null);
		String requestJson = objectMapper.writeValueAsString(testDto);
		MockHttpServletRequestBuilder builder = post("/method-argument-not-valid-exception")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestJson);
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		ErrorResponse<ErrorCode> errorResponse = ErrorResponse.<ErrorCode>builder()
			.errorCode(NOT_VALID_PARAM)
			.msg("ID는 필수 입니다.")
			.build();
		String expectBody = objectMapper.writeValueAsString(errorResponse);
		perform.andExpect(status().isBadRequest())
			.andExpect(content().string(expectBody));
	}

	@Test
	@DisplayName("Handler Method Validation Exception Test")
	void handler_method_validation_exception() throws Exception {
		// given
		MockHttpServletRequestBuilder builder = get("/handler-method-validation-exception")
			.param("num", "0");
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		ErrorResponse<ErrorCode> errorResponse = ErrorResponse.<ErrorCode>builder()
			.errorCode(NOT_VALID_PARAM)
			.msg("must be greater than or equal to 1")
			.build();
		String expectBody = objectMapper.writeValueAsString(errorResponse);
		perform.andExpect(status().isBadRequest())
			.andExpect(content().string(expectBody));
	}

	@Test
	@DisplayName("Type Mismatch Test")
	void typeMismatch() throws Exception {
		// given
		MockHttpServletRequestBuilder builder = get("/type-mismatch")
			.param("num", "A");
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		ErrorResponse<ErrorCode> errorResponse = ErrorResponse.<ErrorCode>builder()
			.errorCode(NOT_VALID_PARAM)
			.msg("Failed to convert value of type 'java.lang.String' to required type 'int'; For input string: \"A\"")
			.build();
		String expectBody = objectMapper.writeValueAsString(errorResponse);
		perform.andExpect(status().isBadRequest())
			.andExpect(content().string(expectBody));
	}

}