package com.cch.codechallengehub.web.mapper.user;

import com.cch.codechallengehub.dto.user.ProfileDto;
import com.cch.codechallengehub.web.dto.user.ProfileResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileResponseMapper {
    ProfileResponse profileDtoToResponse(ProfileDto dto);
}
