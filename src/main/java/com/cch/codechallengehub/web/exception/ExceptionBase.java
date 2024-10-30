package com.cch.codechallengehub.web.exception;

import org.springframework.http.HttpStatus;

public abstract class ExceptionBase extends RuntimeException {

    protected HttpStatus httpStatus;

    public ExceptionBase(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatus() {
        return this.httpStatus;
    }
}
