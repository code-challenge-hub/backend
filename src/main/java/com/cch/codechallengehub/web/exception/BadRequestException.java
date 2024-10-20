package com.cch.codechallengehub.web.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ExceptionBase {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
