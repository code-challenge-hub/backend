package com.cch.codechallengehub.web.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.constants.ChallengeStatus;
import com.cch.codechallengehub.dto.ChallengeSearchResult;
import com.cch.codechallengehub.dto.ChallengeSearchTechStackResult;
import com.cch.codechallengehub.repository.ChallengeQueryRepository;
import com.cch.codechallengehub.service.ChallengeService;
import com.cch.codechallengehub.web.dto.ChallengeCreateRequest;
import com.cch.codechallengehub.web.dto.ChallengeCreateResponse;
import com.cch.codechallengehub.web.dto.ChallengeSearchResponse;
import com.cch.codechallengehub.web.dto.QuestCreateRequest;
import com.cch.codechallengehub.web.exception.ErrorCode;
import com.cch.codechallengehub.web.exception.ErrorResponse;
import com.cch.codechallengehub.web.mapper.ChallengeCreateMapper;
import com.cch.codechallengehub.web.mapper.ChallengeSearchMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(ChallengeController.class)
class ChallengeControllerTest {

	@TestConfiguration
	static class TestConfig {
		@Bean
		public ChallengeSearchMapper challengeSearchMapper() {
			return Mappers.getMapper(ChallengeSearchMapper.class);
		}
	}

	@Autowired
	WebApplicationContext context;

	MockMvc mockMvc;

	ObjectMapper objectMapper;

	@MockBean
	ChallengeService challengeService;

	@SuppressWarnings("unused")
	@MockBean
	ChallengeCreateMapper mapper;

	@MockBean
	ChallengeQueryRepository challengeQueryRepository;

    @Autowired
    ChallengeSearchMapper challengeSearchMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mockMvc = MockMvcBuilders
			.webAppContextSetup(context)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();

	}

	@Test
	void challenge_create_api_success() throws Exception {
	    // given
		when(challengeService.createChallenge(any())).thenReturn(1L);
		LocalDateTime now = LocalDateTime.now();
		QuestCreateRequest quest1 = QuestCreateRequest.builder()
			.questName("quest1")
			.deadline(now.plusDays(9))
			.resultType("URL")
			.orders(1)
			.build();
		QuestCreateRequest quest2 = QuestCreateRequest.builder()
			.questName("quest2")
			.deadline(now.plusDays(7))
			.resultType("IMAGE")
			.orders(2)
			.build();
		ChallengeCreateRequest request = ChallengeCreateRequest.builder()
			.challengeName("test challenge")
			.level("BEGINNER")
			.startDate(now)
			.endDate(now.plusDays(10))
			.recruitType("FIRST_COME_FIRST_SERVE")
			.recruitStartDate(now.plusDays(1))
			.recruitEndDate(now.plusDays(5))
			.recruitNumber(5)
			.funcRequirements(List.of("func1", "func2", "func3"))
			.techStacks(List.of("Spring", "MySQL", "VueJS"))
			.quests(List.of(quest1, quest2))
			.build();
		String requestJson = objectMapper.writeValueAsString(request);
		MockHttpServletRequestBuilder builder = post("/api/v1/challenge")
			.content(requestJson)
			.contentType(APPLICATION_JSON);
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		ChallengeCreateResponse response = new ChallengeCreateResponse(1L);
		String responseJson = objectMapper.writeValueAsString(response);
		perform.andExpect(status().isOk())
			.andExpect(content().string(responseJson))
			.andDo(print());
	}

	@Test
	void challenge_create_api_fail_not_valid_enum() throws Exception {
		// given
		when(challengeService.createChallenge(any())).thenReturn(1L);
		LocalDateTime now = LocalDateTime.now();
		QuestCreateRequest quest1 = QuestCreateRequest.builder()
			.questName("quest1")
			.deadline(now.plusDays(9))
			.resultType("URL")
			.orders(1)
			.build();
		QuestCreateRequest quest2 = QuestCreateRequest.builder()
			.questName("quest2")
			.deadline(now.plusDays(7))
			.resultType("igas") // Not Valid Enum
			.orders(2)
			.build();
		ChallengeCreateRequest request = ChallengeCreateRequest.builder()
			.challengeName("test challenge")
			.level("BEGINNER")
			.startDate(now)
			.endDate(now.plusDays(10))
			.recruitType("1234") // Not Valid Enum
			.recruitStartDate(now.plusDays(1))
			.recruitEndDate(now.plusDays(5))
			.recruitNumber(5)
			.funcRequirements(List.of("func1", "func2", "func3"))
			.techStacks(List.of("Spring", "MySQL", "VueJS"))
			.quests(List.of(quest1, quest2))
			.build();
		String requestJson = objectMapper.writeValueAsString(request);
		MockHttpServletRequestBuilder builder = post("/api/v1/challenge")
			.content(requestJson)
			.contentType(APPLICATION_JSON);
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		ErrorResponse<ErrorCode> errorResponse = ErrorResponse.<ErrorCode>builder()
			.errorCode(ErrorCode.NOT_VALID_PARAM)
			.msg("quest result type is not valid\nrecruit type is not valid")
			.build();
		String responseJson = objectMapper.writeValueAsString(errorResponse);
		perform.andExpect(status().isBadRequest())
			.andExpect(content().string(responseJson))
			.andDo(print());
	}
	
	@Test
	void challenge_create_api_fail_check_required() throws Exception {
		// given
		when(challengeService.createChallenge(any())).thenReturn(1L);
		LocalDateTime now = LocalDateTime.now();
		QuestCreateRequest quest1 = QuestCreateRequest.builder()
			.questName("quest1")
			.deadline(now.plusDays(9))
			.resultType("URL")
			.orders(null) // Not Null
			.build();
		QuestCreateRequest quest2 = QuestCreateRequest.builder()
			.questName("quest2")
			.deadline(now.plusDays(7))
			.resultType("IMAGE")
			.orders(null) // Not Null
			.build();
		ChallengeCreateRequest request = ChallengeCreateRequest.builder()
			.challengeName("test challenge")
			.level("BEGINNER")
			.startDate(now)
			.endDate(now.plusDays(10))
			.recruitType("FIRST_COME_FIRST_SERVE")
			.recruitStartDate(now.plusDays(1))
			.recruitEndDate(null) // Not Null
			.recruitNumber(5)
			.funcRequirements(List.of("func1", "func2", "func3"))
			.techStacks(List.of()) // Not Empty
			.quests(List.of(quest1, quest2))
			.build();
		String requestJson = objectMapper.writeValueAsString(request);
		MockHttpServletRequestBuilder builder = post("/api/v1/challenge")
			.content(requestJson)
			.contentType(APPLICATION_JSON);
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		ErrorResponse<ErrorCode> errorResponse = ErrorResponse.<ErrorCode>builder()
			.errorCode(ErrorCode.NOT_VALID_PARAM)
			.msg("quest orders must be not null\nquest orders must be not null\nrecruit start date must not be null\nchallenge tech stacks must not be empty")
			.build();
		String responseJson = objectMapper.writeValueAsString(errorResponse);
		perform.andExpect(status().isBadRequest())
			.andExpect(content().string(responseJson))
			.andDo(print());
	}
	@Test
	void challenge_create_api_fail_check_required_number() throws Exception {
		// given
		when(challengeService.createChallenge(any())).thenReturn(1L);
		LocalDateTime now = LocalDateTime.now();
		QuestCreateRequest quest1 = QuestCreateRequest.builder()
			.questName("quest1")
			.deadline(now.plusDays(9))
			.resultType("URL")
			.orders(-1) // greater than or equal to 0
			.build();
		QuestCreateRequest quest2 = QuestCreateRequest.builder()
			.questName("quest2")
			.deadline(now.plusDays(7))
			.resultType("IMAGE")
			.orders(0)
			.build();
		ChallengeCreateRequest request = ChallengeCreateRequest.builder()
			.challengeName("test challenge")
			.level("BEGINNER")
			.startDate(now)
			.endDate(now.plusDays(10))
			.recruitType("FIRST_COME_FIRST_SERVE")
			.recruitStartDate(now.plusDays(1))
			.recruitEndDate(now.plusDays(1))
			.recruitNumber(0) // Greater than 0
			.funcRequirements(List.of("func1", "func2", "func3"))
			.techStacks(List.of("func1", "func2", "func3"))
			.quests(List.of(quest1, quest2))
			.build();
		String requestJson = objectMapper.writeValueAsString(request);
		MockHttpServletRequestBuilder builder = post("/api/v1/challenge")
			.content(requestJson)
			.contentType(APPLICATION_JSON);
		// when
		ResultActions perform = mockMvc.perform(builder);
		// then
		ErrorResponse<ErrorCode> errorResponse = ErrorResponse.<ErrorCode>builder()
			.errorCode(ErrorCode.NOT_VALID_PARAM)
			.msg("quest orders must greater than or equal to 0\nrecruit number must be greater than 0")
			.build();
		String responseJson = objectMapper.writeValueAsString(errorResponse);
		perform.andExpect(status().isBadRequest())
			.andExpect(content().string(responseJson))
			.andDo(print());
	}

	@Test
	void challenge_search_api_success() throws Exception {
		// given
		List<ChallengeSearchResult> results = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Long id = (long) i;
			results.add(
				ChallengeSearchResult.builder()
					.challengeId(id)
					.challengeName("name" + i)
					.level(ChallengeLevel.BEGINNER)
					.status(ChallengeStatus.PROCESSING)
					.techStacks(List.of(
						new ChallengeSearchTechStackResult(id, "stack" + i, "icon" + i)
						, new ChallengeSearchTechStackResult(id, "stack" + i, "icon" + i)))
					. createdDate(LocalDateTime.now())
					.build()
			);
		}
		PageRequest pageable = PageRequest.of(1, 10);
		Slice<ChallengeSearchResult> slice = new SliceImpl<>(results, pageable, true);
		when(challengeQueryRepository.findSlice(any(), any())).thenReturn(slice);

		MockHttpServletRequestBuilder builder = get("/api/v1/challenges")
			.param("searchLike", "name")
			.param("status", "PROCESSING")
			.param("sort", "challengeName,ASC")
			.param("page", "3")
			.param("size", "10")
			.contentType(APPLICATION_JSON);

		// when
		ResultActions perform = mockMvc.perform(builder);

		// then
		ChallengeSearchResponse challengeSearchResponse = challengeSearchMapper.sliceToResponse(
			slice);
		String resultJson = objectMapper.writeValueAsString(challengeSearchResponse);
		perform.andExpect(status().isOk())
			.andExpect(content().string(resultJson))
			.andDo(print());
	}

	@Test
	void challenge_search_api_fail_enum_not_valid() throws Exception {
		// given
		MockHttpServletRequestBuilder builder = get("/api/v1/challenges")
			.param("searchLike", "name")
			.param("status", "NOT_STATUS")
			.param("level", "NOT_LEVEL")
			.param("sort", "challengeName,ASC")
			.param("page", "3")
			.param("size", "10")
			.contentType(APPLICATION_JSON);
		// when
		ResultActions perform = mockMvc.perform(builder);

		// then
		ErrorResponse<ErrorCode> errorResponse = ErrorResponse.<ErrorCode>builder()
			.errorCode(ErrorCode.NOT_VALID_PARAM)
			.msg("challenge level is not valid\nchallenge status is not valid")
			.build();
		String responseJson = objectMapper.writeValueAsString(errorResponse);
		perform.andExpect(status().isBadRequest())
			.andExpect(content().string(responseJson))
			.andDo(print());

	}
}