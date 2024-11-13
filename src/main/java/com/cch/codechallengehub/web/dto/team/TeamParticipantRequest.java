package com.cch.codechallengehub.web.dto.team;

import com.cch.codechallengehub.constants.ParticipantsRole;
import com.cch.codechallengehub.web.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamParticipantRequest {

    @NotNull(message = "user Id must be not null")
    private String userId;
    @NotNull(message = "quest result type must be not null")
    @ValidEnum(enumClass = ParticipantsRole.class, message = "role is not valid")
    private String role;
    @NotBlank(message = "position must not be blank")
    private String position;
}
