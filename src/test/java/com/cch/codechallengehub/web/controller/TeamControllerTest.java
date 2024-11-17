package com.cch.codechallengehub.web.controller;


import com.cch.codechallengehub.constants.ParticipantsRole;
import com.cch.codechallengehub.constants.TeamStatus;
import com.cch.codechallengehub.dto.team.*;
import com.cch.codechallengehub.security.CommonSecurityTest;
import com.cch.codechallengehub.service.TeamService;
import com.cch.codechallengehub.web.dto.team.*;
import com.cch.codechallengehub.web.mapper.team.TeamCreateMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@WebMvcTest(controllers  = TeamController.class)
class TeamControllerTest extends CommonSecurityTest {

    @Value("${apiPrefix}") private String BASE_URL;
    @Autowired private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean private TeamService teamService;
    @MockBean private TeamCreateMapper teamCreateMapper;

    @Test
    @WithMockUser
    @DisplayName("인증된사용자 - createTeam API 성공")
    void createTeam_success() throws Exception {
        // given
        Long expectedId = 1L;
        TeamCreateRequest request = new TeamCreateRequest();
        request.setChallengeId(1L);
        request.setTeamName("Test Team");
        request.setParticipants(List.of(
                new TeamParticipantRequest("user1", ParticipantsRole.ADMIN.name(), "BE"),
                new TeamParticipantRequest("user2", ParticipantsRole.USER.name(), "FE")));

        TeamCreateDto dto = TeamCreateDto.builder()
                .challengeId(1L)
                .teamName("Test Team")
                .status(TeamStatus.RUNNING)
                .participants(List.of(
                                new TeamCreateParticipantDto("user1", ParticipantsRole.ADMIN, "BE"),
                                new TeamCreateParticipantDto("user2", ParticipantsRole.USER, "FE")))
                .build();

        when(teamCreateMapper.requestToDto(any(TeamCreateRequest.class))).thenReturn(dto);
        when(teamService.createTeam(any(TeamCreateDto.class))).thenReturn(expectedId);

        String requestJson = objectMapper.writeValueAsString(request);

        // when & then
        TeamCreateResponse response = new TeamCreateResponse(expectedId);
        String responseJson = objectMapper.writeValueAsString(response);
        mockMvc.perform(post(BASE_URL +"/v1/teams")
                        .content(requestJson)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(responseJson));

        verify(teamService, times(1)).createTeam(any(TeamCreateDto.class));
    }

    @Test
    @WithMockUser
    @DisplayName("인증된사용자 - getTeam API 성공")
    void getTeam_success() throws Exception {
        // given
        Long teamId = 1L;
        TeamDto teamDto = TeamDto.builder()
                .teamId(teamId)
                .teamName("Test Team")
                .challengeId(1L)
                .build();

        when(teamService.getTeam(teamId)).thenReturn(teamDto);

        // when & then
        String responseJson = objectMapper.writeValueAsString(teamDto);
        mockMvc.perform(get(BASE_URL +"/v1/teams/{id}", teamId)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(responseJson));
    }

    @Test
    @WithMockUser
    @DisplayName("getTeam API 성공 - team 정보가 없는 경우")
    void getTeam_notFound() throws Exception {
        // given
        Long teamId = 999L;
        when(teamService.getTeam(teamId)).thenReturn(new TeamDto());

        // when & then
        mockMvc.perform(get(BASE_URL +"/v1/teams/{id}", teamId)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamId").isEmpty());
    }

    @Test
    @WithMockUser
    @DisplayName("인증된사용자 - updateTeamOverview API 성공")
    void updateTeamOverview_success() throws Exception {
        // given
        Long teamId = 1L;
        TeamUpdateRequest request = new TeamUpdateRequest();
        request.setOverview("Updated Overview");

        // when & then
        mockMvc.perform(patch(BASE_URL + "/v1/teams/{id}/overview", teamId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(teamService, times(1)).updateTeamOverview(any(String.class), any(TeamOverviewDto.class));
    }

    @Test
    @WithMockUser
    @DisplayName("인증된 사용자 - updateTeamStatus API 성공")
    void updateTeamStatus_success() throws Exception {
        // given
        Long teamId = 1L;
        TeamUpdateRequest request = new TeamUpdateRequest();
        request.setStatus("DONE");

        // when & then
        mockMvc.perform(patch(BASE_URL + "/v1/teams/{id}/status", teamId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(teamService, times(1)).updateTeamStatus(any(String.class), any(TeamStatusDto.class));
    }

    @Test
    @WithMockUser
    @DisplayName("updateTeamStatus API 실패 - 정의되지 않은 상태")
    void updateTeamStatus_invalidStatus() throws Exception {
        // given
        Long teamId = 1L;
        TeamUpdateRequest request = new TeamUpdateRequest();
        request.setStatus("INVALID_STATUS");

        // when & then
        MvcResult mvcResult = mockMvc.perform(patch(BASE_URL + "/v1/teams/{id}/status", teamId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();
        assertInstanceOf(MethodArgumentNotValidException.class, resolvedException);

        assertNotNull(resolvedException.getMessage());
        assertTrue(resolvedException.getMessage().contains("status is not valid"));
    }
}