package com.cch.codechallengehub.service;

import static com.cch.codechallengehub.constants.ChallengeLevel.BEGINNER;
import static com.cch.codechallengehub.constants.QuestResultType.URL;
import static com.cch.codechallengehub.constants.RecruitType.FIRST_COME_FIRST_SERVE;
import static org.assertj.core.api.Assertions.assertThat;

import com.cch.codechallengehub.config.AuditingConfig;
import com.cch.codechallengehub.domain.Challenge;
import com.cch.codechallengehub.domain.Period;
import com.cch.codechallengehub.dto.ChallengeCreateDto;
import com.cch.codechallengehub.dto.ChallengeQuestCreateDto;
import com.cch.codechallengehub.dto.ChallengeTechStackCreateDto;
import com.cch.codechallengehub.repository.ChallengeRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
@Import({AuditingConfig.class})
@Transactional
class ChallengeServiceTest {

	@Autowired
	ChallengeRepository challengeRepository;

	ChallengeService challengeService;

	@BeforeEach
	void setUp() {
		challengeService = new ChallengeService(challengeRepository);
	}


	@Test
	void create_challenge_success() {
	    // given
		LocalDateTime now = LocalDateTime.now();
		ChallengeQuestCreateDto quest1 = ChallengeQuestCreateDto.builder()
			.questName("요구사항 정의")
			.deadline(now.plusHours(10))
			.resultType(URL)
			.orders(1)
			.build();
		List<ChallengeQuestCreateDto> quests = List.of(quest1);

		ChallengeTechStackCreateDto tech1 = ChallengeTechStackCreateDto.builder()
			.stackName("Spring Boot")
			.build();
		List<ChallengeTechStackCreateDto> techStacks = List.of(tech1);

		ChallengeCreateDto createDto = ChallengeCreateDto.builder()
			.challengeName("test Challenge")
			.level(BEGINNER)
			.challengeDesc("test 입니다")
			.period(new Period(now, now.plusDays(2)))
			.recruitType(FIRST_COME_FIRST_SERVE)
			.recruitPeriod(new Period(now, now.plusDays(1)))
			.recruitNumber(10)
			.funcRequirements(List.of("회원가입", "로그인"))
			.quests(quests)
			.techStacks(techStacks)
			.build();
		// when
		Long challengeId = challengeService.createChallenge(createDto);

		// then
		Challenge findChallenge = challengeRepository.findById(challengeId)
			.orElseThrow();
		assertThat(challengeId).isEqualTo(findChallenge.getId());
		assertThat(findChallenge.getChallengeQuests().size()).isEqualTo(1);
		assertThat(findChallenge.getChallengeTechStacks().size()).isEqualTo(1);
		assertThat(findChallenge.getCreatedDate()).isAfter(now);
	}

}