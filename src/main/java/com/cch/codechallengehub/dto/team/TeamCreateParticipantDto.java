package com.cch.codechallengehub.dto.team;

import com.cch.codechallengehub.constants.ParticipantsRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreateParticipantDto {
    private String userId;
    private ParticipantsRole role;
    private String position;
}
