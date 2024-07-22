package com.cch.codechallengehub.service;

import com.cch.codechallengehub.domain.Challenge;
import com.cch.codechallengehub.dto.ChallengeCreateDto;
import com.cch.codechallengehub.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeService {

	private final ChallengeRepository challengeRepository;

	public Long createChallenge(ChallengeCreateDto createDto) {

		Challenge challenge = createDto.toEntity();
		challengeRepository.save(challenge);

		return challenge.getId();
	}

}
