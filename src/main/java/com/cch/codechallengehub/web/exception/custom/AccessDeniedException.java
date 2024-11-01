package com.cch.codechallengehub.web.exception.custom;

import com.cch.codechallengehub.web.exception.ExceptionBase;
import org.springframework.http.HttpStatus;

public class AccessDeniedException extends ExceptionBase {
    public AccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
