package com.cch.codechallengehub.service;

import com.cch.codechallengehub.config.AuditingConfig;
import com.cch.codechallengehub.constants.ParticipantsRole;
import com.cch.codechallengehub.constants.QuestResultType;
import com.cch.codechallengehub.constants.TeamStatus;
import com.cch.codechallengehub.domain.Challenge;
import com.cch.codechallengehub.domain.Period;
import com.cch.codechallengehub.domain.Team;
import com.cch.codechallengehub.domain.User;
import com.cch.codechallengehub.dto.ChallengeCreateDto;
import com.cch.codechallengehub.dto.ChallengeQuestCreateDto;
import com.cch.codechallengehub.dto.ChallengeTechStackCreateDto;
import com.cch.codechallengehub.dto.team.*;
import com.cch.codechallengehub.repository.ChallengeRepository;
import com.cch.codechallengehub.repository.TeamRepository;
import com.cch.codechallengehub.repository.UserRepository;
import com.cch.codechallengehub.web.exception.custom.AccessDeniedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.cch.codechallengehub.constants.ChallengeLevel.BEGINNER;
import static com.cch.codechallengehub.constants.RecruitType.FIRST_COME_FIRST_SERVE;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import({AuditingConfig.class})
@Transactional
class TeamServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChallengeRepository challengeRepository;
    @Autowired
    TeamRepository teamRepository;
    TeamService teamService;

    private final String testUserEmail1 = "test1@test.com";
    private final String testUserEmail2 = "test2@test.com";
    String userId1;
    String userId2;
    Long challengeId;

    @BeforeEach
    void setUp() {
        teamService = new TeamService(userRepository,challengeRepository,teamRepository);
        Challenge challenge = ChallengeCreateDto.builder()
                .challengeName("test Challenge")
                .level(BEGINNER)
                .challengeDesc("test 입니다")
                .period(new Period(LocalDateTime.now(), LocalDateTime.now().plusDays(2)))
                .recruitType(FIRST_COME_FIRST_SERVE)
                .recruitPeriod(new Period(LocalDateTime.now(), LocalDateTime.now().plusDays(1)))
                .recruitNumber(10)
                .funcRequirements(List.of("func1", "func2", "func3"))
                .techStacks(List.of(ChallengeTechStackCreateDto.builder().stackName("java").build()))
                .quests(List.of(ChallengeQuestCreateDto.builder()
                        .questName("quest1")
                        .deadline(LocalDateTime.now().plusDays(9))
                        .resultType(QuestResultType.URL)
                        .orders(1)
                        .build()))
                .build().toEntity();

        challengeRepository.save(challenge);
        challengeId = challenge.getId();

        User user1 = User.builder()
                .email(testUserEmail1)
                .password("1234")
                .build();

        User user2 = User.builder()
                .email(testUserEmail2)
                .password("1234")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        userId1 = user1.getId();
        userId2 = user2.getId();
    }

    @Test
    @DisplayName("team 생성 성공")
    void createTeam_success() {
        // given
        List<TeamCreateParticipantDto> participants = List.of(
                new TeamCreateParticipantDto(userId1, ParticipantsRole.ADMIN, "BE"),
                new TeamCreateParticipantDto(userId2, ParticipantsRole.USER, "FE")
        );

        TeamCreateDto dto = TeamCreateDto.builder()
                .challengeId(challengeId)
                .teamName("Test Team")
                .participants(participants)
                .build();

        // when
        Long resultId = teamService.createTeam(dto);

        // then
        assertNotNull(resultId);
        Team savedTeam = teamRepository.findById(resultId).orElseThrow(() -> new AssertionError("Team not found"));
        assertEquals(dto.getTeamName(), savedTeam.getTeamName());
        assertEquals(dto.getChallengeId(), savedTeam.getChallenge().getId());
        assertEquals(participants.size(), savedTeam.getParticipants().size());
    }

    @Test
    @DisplayName("team 조회 성공")
    void getTeam_success() {
        // given
        List<TeamCreateParticipantDto> participants = List.of(
                new TeamCreateParticipantDto(userId1, ParticipantsRole.ADMIN, "BE"),
                new TeamCreateParticipantDto(userId2, ParticipantsRole.USER, "FE")
        );

        TeamCreateDto dto = TeamCreateDto.builder()
                .challengeId(challengeId)
                .teamName("Test Team")
                .participants(participants)
                .build();

        Long createdTeamId = teamService.createTeam(dto);

        // when
        TeamDto teamDto = teamService.getTeam(createdTeamId);

        // then
        assertNotNull(teamDto);
        assertEquals("Test Team", teamDto.getTeamName());
        assertEquals(challengeId, teamDto.getChallengeId());
        assertEquals(2, teamDto.getParticipants().size());
        assertEquals(userId1, teamDto.getParticipants().get(0).getUserId());
        assertEquals(ParticipantsRole.ADMIN, teamDto.getParticipants().get(0).getRole());
        assertEquals("BE", teamDto.getParticipants().get(0).getPosition());
    }

    @Test
    @DisplayName("team의 개요 수정 성공")
    void updateTeamOverview_success() {
        // given
        List<TeamCreateParticipantDto> participants = List.of(
                new TeamCreateParticipantDto(userId1, ParticipantsRole.ADMIN, "BE"),
                new TeamCreateParticipantDto(userId2, ParticipantsRole.USER, "FE")
        );

        TeamCreateDto dto = TeamCreateDto.builder()
                .challengeId(challengeId)
                .teamName("Test Team")
                .participants(participants)
                .build();

        Long createdTeamId = teamService.createTeam(dto);
        TeamOverviewDto overviewDto = new TeamOverviewDto(createdTeamId, "Updated Overview");

        // when
        teamService.updateTeamOverview(testUserEmail1, overviewDto);

        // then
        Team updatedTeam = teamRepository.findById(createdTeamId).orElseThrow(() -> new AssertionError("Team not found"));
        assertEquals("Updated Overview", updatedTeam.getOverview());
    }

    @Test
    @DisplayName("team의 개요 수정 실패 - 권한 없음")
    void updateTeamOverview_accessDenied() {
        // given
        List<TeamCreateParticipantDto> participants = List.of(
                new TeamCreateParticipantDto(userId1, ParticipantsRole.USER, "BE"),
                new TeamCreateParticipantDto(userId2, ParticipantsRole.USER, "FE")
        );

        TeamCreateDto dto = TeamCreateDto.builder()
                .challengeId(challengeId)
                .teamName("Test Team")
                .participants(participants)
                .build();

        Long createdTeamId = teamService.createTeam(dto);
        TeamOverviewDto overviewDto = new TeamOverviewDto(createdTeamId, "Updated Overview");

        // when & then
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            teamService.updateTeamOverview(testUserEmail1, overviewDto);
        });
        assertEquals("User does not have permission to edit.", exception.getMessage());
    }

    @Test
    @DisplayName("team의 상태 수정 성공")
    void updateTeamStatus_success() {
        // given
        List<TeamCreateParticipantDto> participants = List.of(
                new TeamCreateParticipantDto(userId1, ParticipantsRole.ADMIN, "BE"),
                new TeamCreateParticipantDto(userId2, ParticipantsRole.USER, "FE")
        );

        TeamCreateDto dto = TeamCreateDto.builder()
                .challengeId(challengeId)
                .teamName("Test Team")
                .participants(participants)
                .build();

        Long createdTeamId = teamService.createTeam(dto);
        TeamStatusDto statusDto = new TeamStatusDto(createdTeamId, TeamStatus.RUNNING);

        // when
        teamService.updateTeamStatus(testUserEmail1, statusDto);

        // then
        Team updatedTeam = teamRepository.findById(createdTeamId).orElseThrow(() -> new AssertionError("Team not found"));
        assertEquals(TeamStatus.RUNNING, updatedTeam.getStatus());
    }

    @Test
    @DisplayName("team의 상태 수정 실패 - 권한 없음")
    void updateTeamStatus_accessDenied() {
        // given
        List<TeamCreateParticipantDto> participants = List.of(
                new TeamCreateParticipantDto(userId1, ParticipantsRole.USER, "BE"),
                new TeamCreateParticipantDto(userId2, ParticipantsRole.USER, "FE")
        );

        TeamCreateDto dto = TeamCreateDto.builder()
                .challengeId(challengeId)
                .teamName("Test Team")
                .participants(participants)
                .build();

        Long createdTeamId = teamService.createTeam(dto);
        TeamStatusDto statusDto = new TeamStatusDto(createdTeamId, TeamStatus.RUNNING);

        // when & then
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            teamService.updateTeamStatus(testUserEmail1, statusDto);
        });
        assertEquals("User does not have permission to edit.", exception.getMessage());
    }

}