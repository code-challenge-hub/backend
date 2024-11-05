package com.cch.codechallengehub.dto;

import com.cch.codechallengehub.domain.Challenge;
import com.cch.codechallengehub.domain.ChallengeTechStack;
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
	private String level;
	private String status;
	private String challengeDesc;
	private List<String> techStacks;

	public static ChallengeSearchResult from(Challenge challenge) {
		Long challengeId = challenge.getId();
		String challengeName = challenge.getChallengeName();
		String level = challenge.getLevel().toString();
		String status = challenge.getStatus().toString();
		String challengeDesc = challenge.getChallengeDesc();
		List<String> techStacks = challenge.getChallengeTechStacks().stream()
			.map(ChallengeTechStack::getStackName)
			.toList();

		return ChallengeSearchResult.builder()
			.challengeId(challengeId)
			.challengeName(challengeName)
			.level(level)
			.status(status)
			.challengeDesc(challengeDesc)
			.techStacks(techStacks)
			.build();
	}
}
