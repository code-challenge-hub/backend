package com.cch.codechallengehub.domain;

import static com.cch.codechallengehub.constants.ChallengeStatus.READY;
import static com.cch.codechallengehub.constants.ChallengeStatus.RECRUITING;
import static org.assertj.core.api.Assertions.assertThat;

import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.constants.RecruitType;
import com.cch.codechallengehub.domain.Challenge.ChallengeBuilder;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ChallengeTest {

	/**
	 * 챌린지는  챌린지 명, 레벨, 기간, 모집 기간, 모집 팀 수, 모집 유형, 퀘스트, 기술 스택, 기능 요구사항을 필수로 가진다.
	 */
	@Test
	void challenge_create_success() {
	    // given
		LocalDateTime startDate = LocalDateTime.now();
		LocalDateTime endDate = startDate.plusDays(30);
		Period period = new Period(startDate, endDate);

		LocalDateTime recruitStartDate = startDate.plusDays(1);
		LocalDateTime recruitEndDate = recruitStartDate.plusDays(7);
		Period recruitPeriod = new Period(recruitStartDate, recruitEndDate);
		Recruit recruit = Recruit.builder()
			.period(recruitPeriod)
			.type(RecruitType.FIRST_COME_FIRST_SERVE)
			.number(5)
			.build();

		List<ChallengeQuest> challengeQuests = List.of(new ChallengeQuest());
		List<ChallengeTechStack> challengeTechStacks = List.of(new ChallengeTechStack());
		List<String> funcRequirements = List.of("To-Do");

		ChallengeBuilder builder = Challenge.builder()
			.challengeName("챌린지 One")
			.level(ChallengeLevel.BEGINNER)
			.period(period)
			.recruit(recruit)
			.challengeQuests(challengeQuests)
			.challengeTechStacks(challengeTechStacks)
			.funcRequirements(funcRequirements);
		// when
		Challenge challenge = builder.build();
		// then
		assertThat(challenge).isNotNull();
	}

	@Test
	void challenge_create_fail_without_name() {
		// given
		LocalDateTime startDate = LocalDateTime.now();
		LocalDateTime endDate = startDate.plusDays(30);
		Period period = new Period(startDate, endDate);

		List<ChallengeQuest> challengeQuests = List.of(new ChallengeQuest());
		List<ChallengeTechStack> challengeTechStacks = List.of(new ChallengeTechStack());
		List<String> funcRequirements = List.of("To-Do");

		ChallengeBuilder builder = Challenge.builder()
			.level(ChallengeLevel.BEGINNER)
			.period(period)
			.challengeQuests(challengeQuests)
			.challengeTechStacks(challengeTechStacks)
			.funcRequirements(funcRequirements);
		// when
		AbstractThrowableAssert<?, ? extends Throwable> assertThrow = Assertions.assertThatThrownBy(
			builder::build);
		// then
		assertThrow.isInstanceOf(IllegalArgumentException.class)
			.message().isEqualTo("Challenge name is not null!");
	}

	@Test
	void challenge_create_fail_without_recruit_info() {
		// given
		LocalDateTime startDate = LocalDateTime.now();
		LocalDateTime endDate = startDate.plusDays(30);
		Period period = new Period(startDate, endDate);

		List<ChallengeQuest> challengeQuests = List.of(new ChallengeQuest());
		List<ChallengeTechStack> challengeTechStacks = List.of(new ChallengeTechStack());
		List<String> funcRequirements = List.of("To-Do");

		ChallengeBuilder builder = Challenge.builder()
			.challengeName("Challenge Two")
			.level(ChallengeLevel.BEGINNER)
			.period(period)
			.challengeQuests(challengeQuests)
			.challengeTechStacks(challengeTechStacks)
			.funcRequirements(funcRequirements);
		// when
		AbstractThrowableAssert<?, ? extends Throwable> assertThrow = Assertions.assertThatThrownBy(
			builder::build);
		// then
		assertThrow.isInstanceOf(IllegalArgumentException.class)
			.message().isEqualTo("Challenge recruit info is not null!");
	}

	/**
	 * 처음 생성 시 모집 기간 보다 생성일이 빠르면 READY, 모집 기간 사이에 있으면 RECRUITING
	 */
	@Test
	void challenge_create_status_ready() {
		// given
		LocalDateTime startDate = LocalDateTime.now();
		LocalDateTime endDate = startDate.plusDays(30);
		Period period = new Period(startDate, endDate);

		LocalDateTime recruitStartDate = startDate.plusDays(1);
		LocalDateTime recruitEndDate = recruitStartDate.plusDays(7);
		Period recruitPeriod = new Period(recruitStartDate, recruitEndDate);
		Recruit recruit = Recruit.builder()
			.period(recruitPeriod)
			.type(RecruitType.FIRST_COME_FIRST_SERVE)
			.number(5)
			.build();

		List<ChallengeQuest> challengeQuests = List.of(new ChallengeQuest());
		List<ChallengeTechStack> challengeTechStacks = List.of(new ChallengeTechStack());
		List<String> funcRequirements = List.of("To-Do");

		ChallengeBuilder builder = Challenge.builder()
			.challengeName("Challenge Ready")
			.level(ChallengeLevel.BEGINNER)
			.period(period)
			.recruit(recruit)
			.challengeQuests(challengeQuests)
			.challengeTechStacks(challengeTechStacks)
			.funcRequirements(funcRequirements);
		// when
		Challenge challenge = builder.build();
		// then
		assertThat(challenge.getStatus()).isEqualTo(READY);
	}

	@Test
	void challenge_create_status_recruiting() {
		// given
		LocalDateTime startDate = LocalDateTime.now();
		LocalDateTime endDate = startDate.plusDays(30);
		Period period = new Period(startDate, endDate);

		LocalDateTime recruitStartDate = LocalDateTime.now();
		LocalDateTime recruitEndDate = recruitStartDate.plusDays(7);
		Period recruitPeriod = new Period(recruitStartDate, recruitEndDate);
		Recruit recruit = Recruit.builder()
			.period(recruitPeriod)
			.type(RecruitType.FIRST_COME_FIRST_SERVE)
			.number(5)
			.build();

		List<ChallengeQuest> challengeQuests = List.of(new ChallengeQuest());
		List<ChallengeTechStack> challengeTechStacks = List.of(new ChallengeTechStack());
		List<String> funcRequirements = List.of("To-Do");

		ChallengeBuilder builder = Challenge.builder()
			.challengeName("Challenge Ready")
			.level(ChallengeLevel.BEGINNER)
			.period(period)
			.recruit(recruit)
			.challengeQuests(challengeQuests)
			.challengeTechStacks(challengeTechStacks)
			.funcRequirements(funcRequirements);
		// when
		Challenge challenge = builder.build();
		// then
		assertThat(challenge.getStatus()).isEqualTo(RECRUITING);
	}

}