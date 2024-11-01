package com.cch.codechallengehub.web.mapper.user;

import com.cch.codechallengehub.dto.user.UserDto;
import com.cch.codechallengehub.web.dto.user.UserCreateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserCreateMapper {
    UserDto requestToDto(UserCreateRequest request);
}
