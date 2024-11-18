package com.cch.codechallengehub.dto;

import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.constants.ChallengeStatus;
import com.cch.codechallengehub.domain.Challenge;
import com.cch.codechallengehub.domain.ChallengeTechStack;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeSearchResult {

	private Long challengeId;
	private String challengeName;
	private ChallengeLevel level;
	private ChallengeStatus status;
	private String challengeDesc;
	private List<String> techStacks;
	private LocalDateTime createdDate;

	public static ChallengeSearchResult from(Challenge challenge) {
		Long challengeId = challenge.getId();
		String challengeName = challenge.getChallengeName();
		ChallengeLevel level = challenge.getLevel();
		ChallengeStatus status = challenge.getStatus();
		String challengeDesc = challenge.getChallengeDesc();
		List<String> techStacks = challenge.getChallengeTechStacks().stream()
			.map(ChallengeTechStack::getStackName)
			.toList();
		LocalDateTime createdDate = challenge.getCreatedDate();

		return ChallengeSearchResult.builder()
			.challengeId(challengeId)
			.challengeName(challengeName)
			.level(level)
			.status(status)
			.challengeDesc(challengeDesc)
			.techStacks(techStacks)
			.createdDate(createdDate)
			.build();
	}
}
