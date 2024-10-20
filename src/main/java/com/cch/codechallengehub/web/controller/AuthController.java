package com.cch.codechallengehub.web.controller;

import com.cch.codechallengehub.dto.UserDto;
import com.cch.codechallengehub.service.AuthService;
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

    @PostMapping("/v1/auth/join")
    public ResponseEntity<Void> joinProcess(@RequestBody @Valid UserDto userDto) {
        authService.joinProcess(userDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/auth/reissue")
    public ResponseEntity<Void> reisuue(HttpServletRequest request, HttpServletResponse response) {
        authService.reisuue(request, response);
        return ResponseEntity.ok().build();
    }
}
