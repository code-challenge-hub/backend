package com.cch.codechallengehub.web.mapper.team;

import com.cch.codechallengehub.dto.team.TeamCreateDto;
import com.cch.codechallengehub.web.dto.team.TeamCreateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamCreateMapper {
    TeamCreateDto requestToDto(TeamCreateRequest request);
}
