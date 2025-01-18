package com.cch.codechallengehub.web.controller;

import com.cch.codechallengehub.dto.auth.AuthEmailVerificationDto;
import com.cch.codechallengehub.service.AuthEmailService;
import com.cch.codechallengehub.web.dto.auth.AuthCodeVerificationRequest;
import com.cch.codechallengehub.web.dto.auth.AuthEmailRequest;
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
public class AuthEmailController {
    private final AuthEmailService authEmailService;

    @PostMapping("/v1/auth/email/code")
    public ResponseEntity<Void> sendVerificationCodeEmail(@RequestBody @Valid AuthEmailRequest request) {
        authEmailService.sendVerificationCodeEmail(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/auth/email/verification")
    public ResponseEntity<AuthEmailVerificationDto> verificationCode(@RequestBody @Valid AuthCodeVerificationRequest request) {
        AuthEmailVerificationDto response = authEmailService.verificationCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(response);
    }
}
