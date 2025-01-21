package com.cch.codechallengehub.web.controller;

import com.cch.codechallengehub.dto.ChallengeCreateDto;
import com.cch.codechallengehub.dto.ChallengeSearchCondition;
import com.cch.codechallengehub.dto.ChallengeSearchResult;
import com.cch.codechallengehub.repository.ChallengeQueryRepository;
import com.cch.codechallengehub.service.ChallengeService;
import com.cch.codechallengehub.web.dto.ChallengeCreateRequest;
import com.cch.codechallengehub.web.dto.ChallengeCreateResponse;
import com.cch.codechallengehub.web.dto.ChallengeSearchRequest;
import com.cch.codechallengehub.web.dto.ChallengeSearchResponse;
import com.cch.codechallengehub.web.mapper.ChallengeCreateMapper;
import com.cch.codechallengehub.web.mapper.ChallengeSearchMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	private final ChallengeQueryRepository challengeQueryRepository;
	private final ChallengeSearchMapper challengeSearchMapper;

	@PostMapping("/v1/challenge")
	public ResponseEntity<?> create(@RequestBody @Valid ChallengeCreateRequest request) {
		ChallengeCreateDto challengeCreateDto = challengeCreateMapper.requestToDto(request);
		Long challengeId = challengeService.createChallenge(challengeCreateDto);
		ChallengeCreateResponse response = new ChallengeCreateResponse(challengeId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/v1/challenges")
	public ResponseEntity<?> getChallenges(@Valid ChallengeSearchRequest request, Pageable pageable) {
		ChallengeSearchCondition challengeSearchCondition = challengeSearchMapper.requestToCondition(
			request);
		Slice<ChallengeSearchResult> slice = challengeQueryRepository.findSlice(
			challengeSearchCondition, pageable);
		ChallengeSearchResponse challengeSearchResponse = challengeSearchMapper.sliceToResponse(
			slice);
		return ResponseEntity.ok(challengeSearchResponse);
	}

}
