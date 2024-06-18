package com.cch.codechallengehub.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationTestController {

	@PostMapping("/api/v1/auth/login")
	public void login() {
	}

	@GetMapping("/need/authentication")
	public void needAuthentication() {

	}

}
