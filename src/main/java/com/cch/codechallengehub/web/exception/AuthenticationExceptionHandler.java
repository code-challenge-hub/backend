package com.cch.codechallengehub.web.exception;

import static com.cch.codechallengehub.web.exception.ErrorCode.NO_AUTHENTICATION;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class AuthenticationExceptionHandler {

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse<ErrorCode>> handleBadCredentialsException(BadCredentialsException ex) {
		ErrorResponse<ErrorCode> errorResponse = ErrorResponse.<ErrorCode>builder()
			.errorCode(NO_AUTHENTICATION)
			.msg(ex.getMessage())
			.build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	}

}
