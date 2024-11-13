package com.cch.codechallengehub.dto.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamOverviewDto {
    private Long teamId;
    private String overview;
}
