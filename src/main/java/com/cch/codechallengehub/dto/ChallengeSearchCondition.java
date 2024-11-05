package com.cch.codechallengehub.dto;

import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.constants.ChallengeStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeSearchCondition {

	private String challengeNameLike;
	private List<String> techStacks;
	private ChallengeStatus status;
	private ChallengeLevel level;

}
