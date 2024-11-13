package com.cch.codechallengehub.web.dto.team;

import com.cch.codechallengehub.constants.TeamStatus;
import com.cch.codechallengehub.web.validation.ValidEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamUpdateRequest {

    private String overview;
    @ValidEnum(enumClass = TeamStatus.class, message = "status is not valid")
    private String status;
}
