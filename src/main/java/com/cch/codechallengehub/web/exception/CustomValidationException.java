package com.cch.codechallengehub.web.exception;

import org.springframework.http.HttpStatus;


public class CustomValidationException extends RuntimeException {
    private final HttpStatus status;

    public CustomValidationException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public CustomValidationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
