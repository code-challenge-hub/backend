package com.cch.codechallengehub.web.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ExceptionBase {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

