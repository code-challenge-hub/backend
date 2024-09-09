package com.cch.codechallengehub.web.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends ExceptionBase {
    public AuthException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public AuthException(String message, HttpStatus status) {
        super(message, status);
    }
}