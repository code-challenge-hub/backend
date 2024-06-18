package com.cch.codechallengehub.security;

import lombok.Getter;

@Getter
public enum AuthorizationHeader {

	AUTHORIZATION("Authorization"),
	BEARER("Bearer");

	private final String value;

	AuthorizationHeader(String value) {
		this.value = value;
	}

}
