package com.cch.codechallengehub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;

	public String authenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			email, password);
		Authentication authenticate;
		try {
			authenticate = authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException ex) {
			throw new BadCredentialsException("Email, Password Not Valid");
		}
		String userEmail = authenticate.getName();

		return jwtTokenProvider.createToken(userEmail);
	}

}
