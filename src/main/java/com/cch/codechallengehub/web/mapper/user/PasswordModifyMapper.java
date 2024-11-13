package com.cch.codechallengehub.web.mapper.user;

import com.cch.codechallengehub.dto.user.PasswordDto;
import com.cch.codechallengehub.web.dto.user.PasswordModifyRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PasswordModifyMapper {
    PasswordDto requestToDto(PasswordModifyRequest request);
}
