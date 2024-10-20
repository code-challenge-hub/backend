package com.cch.codechallengehub.controller;

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
@RequestMapping(path = "${apiPrefix}/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<Void> joinProcess(@RequestBody @Valid UserDto userDto) {
        authService.joinProcess(userDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<Void> reisuue(HttpServletRequest request, HttpServletResponse response) {
        authService.reisuue(request, response);
        return ResponseEntity.ok().build();
    }
}
