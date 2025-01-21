package com.cch.codechallengehub.web.mapper;


import static org.assertj.core.api.Assertions.assertThat;

import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.constants.ChallengeStatus;
import com.cch.codechallengehub.dto.ChallengeSearchCondition;
import com.cch.codechallengehub.dto.ChallengeSearchResult;
import com.cch.codechallengehub.dto.ChallengeSearchTechStackResult;
import com.cch.codechallengehub.web.dto.ChallengeSearchItem;
import com.cch.codechallengehub.web.dto.ChallengeSearchRequest;
import com.cch.codechallengehub.web.dto.ChallengeSearchResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

class ChallengeSearchMapperTest {

    ChallengeSearchMapper mapper = Mappers.getMapper(ChallengeSearchMapper.class);

    @Test
    void requestToConditionTest() {
        ChallengeSearchRequest request = new ChallengeSearchRequest();
        request.setSearchLike("search");
        request.setLevel("BEGINNER");
        request.setStatus("PROCESSING");

        ChallengeSearchCondition challengeSearchCondition = mapper.requestToCondition(request);
        assertThat(request.getSearchLike()).isEqualTo(challengeSearchCondition.getSearchLike());
        assertThat(request.getLevel()).isEqualTo(challengeSearchCondition.getLevel().toString());
        assertThat(request.getStatus()).isEqualTo(challengeSearchCondition.getStatus().toString());
    }

    @Test
    void sliceToResponseTest() {
        List<ChallengeSearchResult> results = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Long id = (long) i;
            results.add(
                ChallengeSearchResult.builder()
                    .challengeId(id)
                    .challengeName("name" + i)
                    .level(ChallengeLevel.BEGINNER)
                    .status(ChallengeStatus.PROCESSING)
                    .techStacks(List.of(
                        new ChallengeSearchTechStackResult(id, "stack" + i, "icon" + i)
                        , new ChallengeSearchTechStackResult(id, "stack" + i, "icon" + i)))
                    . createdDate(LocalDateTime.now())
                    .build()
            );
        }
        PageRequest pageable = PageRequest.of(1, 10);
        Slice<ChallengeSearchResult> slice = new SliceImpl<>(results, pageable, true);
        ChallengeSearchResponse challengeSearchResponse = mapper.sliceToResponse(slice);

        List<ChallengeSearchItem> items = challengeSearchResponse.getContents();
        int size = challengeSearchResponse.getSize();
        int page = challengeSearchResponse.getPage();
        boolean hasNext = challengeSearchResponse.isHasNext();

        assertThat(size).isEqualTo(slice.getSize());
        assertThat(page).isEqualTo(slice.getNumber());
        assertThat(hasNext).isEqualTo(slice.hasNext());

        for (int i = 0; i < items.size(); i++) {
            ChallengeSearchItem item = items.get(i);
            assertThat(item.getChallengeId()).isEqualTo(results.get(i).getChallengeId());
            assertThat(item.getChallengeName()).isEqualTo(results.get(i).getChallengeName());
            assertThat(item.getLevel()).isEqualTo(results.get(i).getLevel().toString());
            assertThat(item.getStatus()).isEqualTo(results.get(i).getStatus().toString());
            assertThat(item.getTechStacks().get(0).getStackName()).isEqualTo(results.get(i).getTechStacks().get(0).getStackName());
        }
    }
}