package com.cch.codechallengehub.dto.team;

import com.cch.codechallengehub.constants.TeamStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreateDto {
    private Long challengeId;
    private String teamName;
    @Builder.Default
    private TeamStatus status = TeamStatus.RUNNING;
    private List<TeamCreateParticipantDto> participants = new ArrayList<>();
}
