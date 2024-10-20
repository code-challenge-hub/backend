package com.cch.codechallengehub.web.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.cch.codechallengehub.dto.ChallengeCreateDto;
import com.cch.codechallengehub.dto.ChallengeQuestCreateDto;
import com.cch.codechallengehub.web.dto.ChallengeCreateRequest;
import com.cch.codechallengehub.web.dto.QuestCreateRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ChallengeCreateMapperTest {

	ChallengeCreateMapper challengeCreateMapper = Mappers.getMapper(ChallengeCreateMapper.class);

	@Test
	void requestToDto_basic() {
	    // given
		ChallengeCreateRequest request = ChallengeCreateRequest.builder()
			.challengeName("a")
			.challengeDesc("b")
			.level("BEGINNER")
			.funcRequirements(List.of("1", "2"))
			.techStacks(List.of("Spring, Java, React.js"))
			.build();
		// when
		ChallengeCreateDto challengeCreateDto = challengeCreateMapper.requestToDto(request);
		// then
		assertThat(request.getChallengeName()).isEqualTo(challengeCreateDto.getChallengeName());
		assertThat(request.getChallengeDesc()).isEqualTo(challengeCreateDto.getChallengeDesc());
		assertThat(request.getLevel()).isEqualTo(challengeCreateDto.getLevel().toString());
		assertThat(request.getFuncRequirements().size()).isEqualTo(challengeCreateDto.getFuncRequirements().size());
		assertThat(request.getFuncRequirements().get(0)).isEqualTo(challengeCreateDto.getFuncRequirements().get(0));
		assertThat(request.getTechStacks().size()).isEqualTo(challengeCreateDto.getTechStacks().size());
		assertThat(request.getTechStacks().get(0)).isEqualTo(challengeCreateDto.getTechStacks().get(0).getStackName());
	}

	@Test
	void requestToDto_period() {
		// given
		LocalDateTime now = LocalDateTime.now();
		ChallengeCreateRequest request = ChallengeCreateRequest.builder()
			.startDate(now)
			.endDate(now.plusDays(10))
			.recruitStartDate(now)
			.recruitEndDate(now.plusDays(3))
			.build();
		// when
		ChallengeCreateDto challengeCreateDto = challengeCreateMapper.requestToDto(request);
		// then
		assertThat(request.getStartDate()).isEqualTo(challengeCreateDto.getPeriod().getStartDate());
		assertThat(request.getEndDate()).isEqualTo(challengeCreateDto.getPeriod().getEndDate());
		assertThat(request.getRecruitStartDate()).isEqualTo(challengeCreateDto.getRecruitPeriod().getStartDate());
		assertThat(request.getRecruitEndDate()).isEqualTo(challengeCreateDto.getRecruitPeriod().getEndDate());
	}

	@Test
	void requestToDto_questDtoList() {
	    // given
		LocalDateTime now = LocalDateTime.now();
		QuestCreateRequest questRequest1 = QuestCreateRequest.builder()
			.questName("1")
			.deadline(now.plusDays(2))
			.resultType("URL")
			.orders(1)
			.build();
		QuestCreateRequest questRequest2 = QuestCreateRequest.builder()
			.questName("2")
			.deadline(now.plusDays(4))
			.resultType("IMAGE")
			.orders(2)
			.build();
		List<QuestCreateRequest> quests = List.of(questRequest1, questRequest2);
		ChallengeCreateRequest request = ChallengeCreateRequest.builder()
			.quests(quests)
			.build();
		// when
		ChallengeCreateDto challengeCreateDto = challengeCreateMapper.requestToDto(request);
		// then
		List<ChallengeQuestCreateDto> questDtoList = challengeCreateDto.getQuests();
		assertThat(questDtoList.size()).isEqualTo(quests.size());
		assertThat(questDtoList.get(0).getQuestName()).isEqualTo(quests.get(0).getQuestName());
		assertThat(questDtoList.get(1).getQuestName()).isEqualTo(quests.get(1).getQuestName());
	}


}