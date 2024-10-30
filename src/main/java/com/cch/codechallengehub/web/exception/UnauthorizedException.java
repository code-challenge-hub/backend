package com.cch.codechallengehub.web.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ExceptionBase {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
