package com.cch.codechallengehub.web.dto;

import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.constants.ChallengeStatus;
import com.cch.codechallengehub.web.validation.ValidEnum;
import lombok.Data;

@Data
public class ChallengeSearchRequest {

    private String searchLike;

    @ValidEnum(enumClass = ChallengeLevel.class, message = "challenge level is not valid")
    private String level;

    @ValidEnum(enumClass = ChallengeStatus.class, message = "challenge status is not valid")
    private String status;
}
