package com.cch.codechallengehub.dto;

import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.constants.ChallengeStatus;
import com.cch.codechallengehub.constants.RecruitType;
import com.cch.codechallengehub.domain.Challenge;
import com.cch.codechallengehub.domain.ChallengeQuest;
import com.cch.codechallengehub.domain.ChallengeTechStack;
import com.cch.codechallengehub.domain.Period;
import com.cch.codechallengehub.domain.Recruit;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeCreateDto {

	private String challengeName;
	private ChallengeLevel level;
	private String challengeDesc;
	private Period period;
	private RecruitType recruitType;
	private Period recruitPeriod;
	private Integer recruitNumber;
	private List<String> funcRequirements;

	private List<ChallengeQuestCreateDto> quests = new ArrayList<>();
	private List<ChallengeTechStackCreateDto> techStacks = new ArrayList<>();

	public Challenge toEntity() {

		Recruit recruit = Recruit.builder()
			.period(recruitPeriod)
			.type(recruitType)
			.number(recruitNumber)
			.build();
		List<ChallengeQuest> quests = this.quests.stream()
			.map(ChallengeQuestCreateDto::toEntity)
			.toList();
		List<ChallengeTechStack> techStacks = this.techStacks.stream()
			.map(ChallengeTechStackCreateDto::toEntity)
			.toList();

		return Challenge.builder()
			.challengeName(challengeName)
			.level(level)
			.challengeDesc(challengeDesc)
			.period(period)
			.recruit(recruit)
			.status(ChallengeStatus.READY)
			.challengeQuests(quests)
			.challengeTechStacks(techStacks)
			.funcRequirements(funcRequirements)
			.build();
	}

}
