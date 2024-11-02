package com.cch.codechallengehub.web.controller;

import com.cch.codechallengehub.constants.TeamStatus;
import com.cch.codechallengehub.dto.team.TeamCreateDto;
import com.cch.codechallengehub.dto.team.TeamDto;
import com.cch.codechallengehub.dto.team.TeamOverviewDto;
import com.cch.codechallengehub.dto.team.TeamStatusDto;
import com.cch.codechallengehub.service.TeamService;
import com.cch.codechallengehub.web.dto.team.TeamCreateRequest;
import com.cch.codechallengehub.web.dto.team.TeamCreateResponse;
import com.cch.codechallengehub.web.dto.team.TeamUpdateRequest;
import com.cch.codechallengehub.web.mapper.team.TeamCreateMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${apiPrefix}")
public class TeamController {

    private final TeamService teamService;
    private final TeamCreateMapper teamCreateMapper;

    // 팀 생성
    @PostMapping("/v1/teams")
    public ResponseEntity<TeamCreateResponse> createTeam(@Valid @RequestBody TeamCreateRequest request) {
        TeamCreateDto dto = teamCreateMapper.requestToDto(request);
        Long id = teamService.createTeam(dto);
        TeamCreateResponse response = new TeamCreateResponse(id);
        return ResponseEntity.ok(response);
    }

    // 팀 상세 조회
    @GetMapping("/v1/teams/{id}")
    public ResponseEntity<TeamDto> getTeam(@PathVariable("id") Long id) {
        TeamDto response = teamService.getTeam(id);
        return ResponseEntity.ok(response);
    }

    // 팀 소개 수정
    @PostMapping("/v1/teams/{id}/overview")
    public ResponseEntity<?> updateTeamOverview(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long id, @RequestBody TeamUpdateRequest request) {
        TeamOverviewDto dto = TeamOverviewDto.builder().teamId(id).overview(request.getOverview()).build();
        teamService.updateTeamOverview(userDetails.getUsername(), dto);
        return ResponseEntity.ok().build();
    }

    // 팀 상태 수정
    @PostMapping("/v1/teams/{id}/status")
    public ResponseEntity<?> updateTeamStatus(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long id, @Valid @RequestBody TeamUpdateRequest request) {
        TeamStatusDto dto = TeamStatusDto.builder().teamId(id).status((TeamStatus) Enum.valueOf(TeamStatus.class, request.getStatus())).build();
        teamService.updateTeamStatus(userDetails.getUsername(), dto);
        return ResponseEntity.ok().build();
    }

}
