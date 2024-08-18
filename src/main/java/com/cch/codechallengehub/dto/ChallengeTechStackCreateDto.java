package com.cch.codechallengehub.dto;

import com.cch.codechallengehub.domain.ChallengeTechStack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeTechStackCreateDto {

	private String stackName;

	public ChallengeTechStack toEntity() {
		return ChallengeTechStack.builder()
			.stackName(stackName)
			.build();
	}

}
