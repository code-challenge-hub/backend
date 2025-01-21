package com.cch.codechallengehub.dto;

import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.constants.ChallengeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeSearchCondition {

	private String searchLike;
	private ChallengeStatus status;
	private ChallengeLevel level;

}
