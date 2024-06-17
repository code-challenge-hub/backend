package com.cch.codechallengehub.service;

import com.cch.codechallengehub.exception.NotAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
		} catch (AuthenticationException ex) {
			throw new NotAuthenticationException(ex.getMessage());
		}
		String userEmail = authenticate.getName();

		return jwtTokenProvider.createToken(userEmail);
	}

}
