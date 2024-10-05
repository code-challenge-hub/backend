package com.cch.codechallengehub.web.exception;

import org.springframework.http.HttpStatus;

public class TokenException extends ExceptionBase {

    public TokenException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public TokenException(String message, HttpStatus status) {
        super(message, status);
    }
}
