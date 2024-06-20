package com.cch.codechallengehub.web.controller;

import static com.cch.codechallengehub.security.AuthorizationHeader.AUTHORIZATION;
import static com.cch.codechallengehub.security.AuthorizationHeader.BEARER;

import com.cch.codechallengehub.service.AuthService;
import com.cch.codechallengehub.web.dto.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

		String email = request.getEmail();
		String password = request.getPassword();

		String token = authService.authenticate(email, password);
		String bearerToken = BEARER.getValue() + " " + token;

		return ResponseEntity.ok()
			.header(AUTHORIZATION.getValue(), bearerToken)
			.build();
	}

}
