package com.cch.codechallengehub.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(2)
public class GlobalExceptionHandler {
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse<ErrorCode>> handleRuntimeException(RuntimeException ex) {

		String message = ex.getMessage();
		ErrorResponse<ErrorCode> response = ErrorResponse.<ErrorCode>builder()
			.errorCode(ErrorCode.UNKNOWN_ERROR)
			.msg(message)
			.build();

		return ResponseEntity.internalServerError().body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse<ErrorCode>> handleException(Exception ex) {

		String message = ex.getMessage();
		ErrorResponse<ErrorCode> response = ErrorResponse.<ErrorCode>builder()
			.errorCode(ErrorCode.UNKNOWN_ERROR)
			.msg(message)
			.build();

		return ResponseEntity.internalServerError().body(response);
	}

}
