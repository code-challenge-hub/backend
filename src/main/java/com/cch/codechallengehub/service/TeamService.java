package com.cch.codechallengehub.service;

import com.cch.codechallengehub.constants.ParticipantsRole;
import com.cch.codechallengehub.domain.Challenge;
import com.cch.codechallengehub.domain.Participants;
import com.cch.codechallengehub.domain.Team;
import com.cch.codechallengehub.domain.User;
import com.cch.codechallengehub.dto.team.*;
import com.cch.codechallengehub.repository.ChallengeRepository;
import com.cch.codechallengehub.repository.TeamRepository;
import com.cch.codechallengehub.repository.UserRepository;
import com.cch.codechallengehub.web.exception.custom.AccessDeniedException;
import com.cch.codechallengehub.web.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class TeamService {
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public Long createTeam(TeamCreateDto dto) {

        //challenge id 검증
        Challenge challenge = challengeRepository.findById(dto.getChallengeId())
                .orElseThrow(() -> new BadRequestException("Challenge not found with id"));

        //user id 검증
        List<Participants> participants = dto.getParticipants().stream()
                .map(participantDto -> {
                    User user = userRepository.findById(participantDto.getUserId())
                            .orElseThrow(() -> new BadRequestException("User not found with id: " + participantDto.getUserId()));
                    return Participants.builder()
                            .user(user)
                            .role(participantDto.getRole())
                            .position(participantDto.getPosition())
                            .build();
                })
                .collect(Collectors.toList());

        Team team = Team.builder()
                .challenge(challenge)
                .teamName(dto.getTeamName())
                .status(dto.getStatus())
                .participants(participants)
                .build();

        teamRepository.save(team);
        return team.getId();
    }

    public TeamDto getTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("team not found with id"));

        return TeamDto.builder()
                .teamId(team.getId())
                .teamName(team.getTeamName())
                .challengeId(team.getChallenge().getId())
                .status(team.getStatus())
                .participants(team.getParticipants().stream()
                        .map(participant -> TeamParticipantDto.builder()
                                .userId(participant.getUser().getId())
                                .nickname(participant.getUser().getNickname())
                                .email(participant.getUser().getEmail())
                                .role(participant.getRole())
                                .position(participant.getPosition())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void updateTeamOverview(String username, TeamOverviewDto dto) {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new BadRequestException("team not found with id"));

        //user가 admin 권한인지 확인
        boolean isAdmin = team.getParticipants().stream()
                .anyMatch(participant ->
                        participant.getUser().getEmail().equals(username) &&
                                participant.getRole() == ParticipantsRole.ADMIN
                );

        if (!isAdmin) {
            throw new AccessDeniedException("User does not have permission to edit.");
        }
        //수정
        team.updateOverview(dto.getOverview());

    }

    @Transactional
    public void updateTeamStatus(String username, TeamStatusDto dto) {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new BadRequestException("team not found with id"));

        //user가 admin 권한인지 확인
        boolean isAdmin = team.getParticipants().stream()
                .anyMatch(participant ->
                        participant.getUser().getEmail().equals(username) &&
                                participant.getRole() == ParticipantsRole.ADMIN
                );

        if (!isAdmin) {
            throw new AccessDeniedException("User does not have permission to edit.");
        }
        //수정
        team.updateStatus(dto.getStatus());
    }
}
