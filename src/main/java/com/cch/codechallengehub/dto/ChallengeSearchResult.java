package com.cch.codechallengehub.dto;

import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.constants.ChallengeStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeSearchResult {

    private Long challengeId;
    private String challengeName;
    private ChallengeLevel level;
    private ChallengeStatus status;
    private String challengeDesc;
    private List<ChallengeSearchTechStackResult> techStacks;
    private LocalDateTime createdDate;
    private Long views;
    private byte[] thumbnail;
    private boolean liked;
    private int likeCount;
    private int participantsNum;
    private Integer recruitNum;

}
