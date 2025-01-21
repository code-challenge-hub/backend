package com.cch.codechallengehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeSearchTechStackResult {

    private Long challengeId;
    private String stackName;
    private String icon;

}
