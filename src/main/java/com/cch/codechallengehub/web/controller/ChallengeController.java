package com.cch.codechallengehub.web.controller;

import com.cch.codechallengehub.dto.ChallengeCreateDto;
import com.cch.codechallengehub.service.ChallengeService;
import com.cch.codechallengehub.web.dto.ChallengeCreateRequest;
import com.cch.codechallengehub.web.dto.ChallengeCreateResponse;
import com.cch.codechallengehub.web.mapper.ChallengeCreateMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class ChallengeController {

	private final ChallengeService challengeService;
	private final ChallengeCreateMapper challengeCreateMapper;

	@PostMapping("/v1/challenge")
	public ResponseEntity<?> create(@RequestBody @Valid ChallengeCreateRequest request) {
		ChallengeCreateDto challengeCreateDto = challengeCreateMapper.requestToDto(request);
		Long challengeId = challengeService.createChallenge(challengeCreateDto);
		ChallengeCreateResponse response = new ChallengeCreateResponse(challengeId);
		return ResponseEntity.ok(response);
	}

}
