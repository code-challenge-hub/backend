package com.cch.codechallengehub.api.exception;

import static com.cch.codechallengehub.api.exception.ErrorCode.UNKNOWN_ERROR;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(2)
public class GlobalExceptionHandler extends MvcExceptionHandler {
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse<ErrorCode>> handleRuntimeException(RuntimeException ex) {

		String message = ex.getMessage();
		ErrorResponse<ErrorCode> response = ErrorResponse.<ErrorCode>builder()
			.errorCode(UNKNOWN_ERROR)
			.msg(message)
			.build();

		return ResponseEntity.internalServerError().body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse<ErrorCode>> handleException(Exception ex) {

		String message = ex.getMessage();
		ErrorResponse<ErrorCode> response = ErrorResponse.<ErrorCode>builder()
			.errorCode(UNKNOWN_ERROR)
			.msg(message)
			.build();

		return ResponseEntity.internalServerError().body(response);
	}

}
