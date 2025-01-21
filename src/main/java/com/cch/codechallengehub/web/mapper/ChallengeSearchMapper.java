package com.cch.codechallengehub.web.mapper;

import com.cch.codechallengehub.dto.ChallengeSearchCondition;
import com.cch.codechallengehub.dto.ChallengeSearchResult;
import com.cch.codechallengehub.web.dto.ChallengeSearchRequest;
import com.cch.codechallengehub.web.dto.ChallengeSearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Slice;

@Mapper(componentModel = "spring")
public interface ChallengeSearchMapper {

    ChallengeSearchCondition requestToCondition(ChallengeSearchRequest request);

    @Mapping(target = "contents", source = "content")
    @Mapping(target = "page", expression = "java(slice.getNumber())")
    @Mapping(target = "size", expression = "java(slice.getSize())")
    @Mapping(target = "hasNext", expression = "java(slice.hasNext())")
    ChallengeSearchResponse sliceToResponse(Slice<ChallengeSearchResult> slice);

}
