package com.cch.codechallengehub.web.exception.custom;

import com.cch.codechallengehub.web.exception.ExceptionBase;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ExceptionBase {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
