package com.cch.codechallengehub.web.dto.team;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamCreateRequest {

    @NotNull(message = "challenge Id must be not null")
    private Long challengeId;

    @NotBlank(message = "team name must not be blank")
    private String teamName;

    @Valid
    @NotEmpty(message = "team participant must not be empty")
    private List<TeamParticipantRequest> participants = new ArrayList<>();

}
