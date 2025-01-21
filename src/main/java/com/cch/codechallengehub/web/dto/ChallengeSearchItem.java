package com.cch.codechallengehub.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ChallengeSearchItem {

    private Long challengeId;
    private String challengeName;
    private String level;
    private String status;
    private String challengeDesc;
    private List<ChallengeSearchTechStackItem> techStacks;
    private LocalDateTime createdDate;
    private Long views;
    private byte[] thumbnail;
    private boolean liked;
    private int likeCount;
    private int participantsNum;
    private int recruitNum;

}
