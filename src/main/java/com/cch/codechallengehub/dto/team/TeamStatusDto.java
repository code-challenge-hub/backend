package com.cch.codechallengehub.dto.team;

import com.cch.codechallengehub.constants.TeamStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamStatusDto {
    private Long teamId;
    private TeamStatus status;
}
