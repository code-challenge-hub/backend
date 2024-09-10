package com.cch.codechallengehub.controller;

import com.cch.codechallengehub.dto.JoinDTO;
import com.cch.codechallengehub.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping(path = "${apiPrefix}/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(JoinDTO joinDTO) {
        authService.joinProcess(joinDTO);
        return ResponseEntity.ok()
                .build();
    }

}
