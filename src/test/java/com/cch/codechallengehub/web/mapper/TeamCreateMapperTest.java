package com.cch.codechallengehub.web.mapper;

import com.cch.codechallengehub.constants.ParticipantsRole;
import com.cch.codechallengehub.constants.TeamStatus;
import com.cch.codechallengehub.dto.team.TeamCreateDto;
import com.cch.codechallengehub.dto.team.TeamCreateParticipantDto;
import com.cch.codechallengehub.web.dto.team.TeamCreateRequest;
import com.cch.codechallengehub.web.dto.team.TeamParticipantRequest;
import com.cch.codechallengehub.web.mapper.team.TeamCreateMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeamCreateMapperTest {
    TeamCreateMapper mapper = Mappers.getMapper(TeamCreateMapper.class);

    @Test
    void requestToDto_default() {
        List<TeamParticipantRequest> participants = List.of(
                new TeamParticipantRequest("user1", ParticipantsRole.ADMIN.name(), "BE"),
                new TeamParticipantRequest("user2", ParticipantsRole.USER.name(), "FE")
        );

        TeamCreateRequest request = TeamCreateRequest.builder()
                .challengeId(1L)
                .teamName("Team A")
                .participants(participants)
                .build();

        TeamCreateDto dto = mapper.requestToDto(request);

        assertEquals(dto.getChallengeId(), request.getChallengeId());
        assertEquals(dto.getTeamName(), request.getTeamName());
        assertEquals(dto.getStatus(), TeamStatus.RUNNING);
        assertEquals(dto.getParticipants().size(), request.getParticipants().size());

        for (int i = 0; i < request.getParticipants().size(); i++) {
            TeamParticipantRequest reqParticipant = request.getParticipants().get(i);
            TeamCreateParticipantDto dtoParticipant = dto.getParticipants().get(i);

            assertEquals(reqParticipant.getUserId(), dtoParticipant.getUserId());
            assertEquals(ParticipantsRole.valueOf(reqParticipant.getRole()), dtoParticipant.getRole());
            assertEquals(reqParticipant.getPosition(), dtoParticipant.getPosition());
        }
    }
}
