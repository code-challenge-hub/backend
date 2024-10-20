package com.cch.codechallengehub.web.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends ExceptionBase {
    public AccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
