package com.cch.codechallengehub.web.exception.custom;

import com.cch.codechallengehub.web.exception.ExceptionBase;
import org.springframework.http.HttpStatus;

public class AwsSesException extends ExceptionBase {
    public AwsSesException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}