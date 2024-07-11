package com.cch.codechallengehub.exception;

public class DuplicateChallengeNameException extends RuntimeException {

	public DuplicateChallengeNameException() {
		super();
	}

	public DuplicateChallengeNameException(String message) {
		super(message);
	}
}
