package com.cch.codechallengehub.web.exception.custom;

import com.cch.codechallengehub.web.exception.ExceptionBase;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ExceptionBase {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
