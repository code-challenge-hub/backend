package com.cch.codechallengehub.web.controller;

import com.cch.codechallengehub.dto.user.UserDto;
import com.cch.codechallengehub.service.AuthService;
import com.cch.codechallengehub.web.dto.user.UserCreateRequest;
import com.cch.codechallengehub.web.mapper.user.UserCreateMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${apiPrefix}")
public class AuthController {

    private final AuthService authService;
    private final UserCreateMapper userCreateMapper;

    @PostMapping("/v1/auth/join")
    public ResponseEntity<Void> joinProcess(@RequestBody @Valid UserCreateRequest request) {
        UserDto userDto = userCreateMapper.requestToDto(request);
        authService.joinProcess(userDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/auth/reissue")
    public ResponseEntity<Void> reisuue(HttpServletRequest request, HttpServletResponse response) {
        authService.reisuue(request, response);
        return ResponseEntity.ok().build();
    }
}
