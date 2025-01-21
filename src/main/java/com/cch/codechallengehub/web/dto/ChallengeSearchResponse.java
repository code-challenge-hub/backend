package com.cch.codechallengehub.web.dto;

import java.util.List;
import lombok.Data;

@Data
public class ChallengeSearchResponse {

    private List<ChallengeSearchItem> contents;
    private int page;
    private int size;
    private boolean hasNext;

}
