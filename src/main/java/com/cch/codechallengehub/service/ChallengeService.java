package com.cch.codechallengehub.service;

import com.cch.codechallengehub.domain.Challenge;
import com.cch.codechallengehub.dto.ChallengeCreateDto;
import com.cch.codechallengehub.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {

	private final ChallengeRepository challengeRepository;

	@Transactional
	public Long createChallenge(ChallengeCreateDto createDto) {

		Challenge challenge = createDto.toEntity();
		challengeRepository.save(challenge);

		return challenge.getId();
	}

}
