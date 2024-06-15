package com.cch.codechallengehub.api.exception;

import static com.cch.codechallengehub.api.exception.ErrorCode.NOT_VALID_PARAM;
import static com.cch.codechallengehub.api.exception.ErrorCode.NO_FOUND_RESOURCE;
import static com.cch.codechallengehub.api.exception.ErrorCode.UNKNOWN_ERROR;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
@Order(2)
public class GlobalExceptionHandler {
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse<ErrorCode>> handleRuntimeException(RuntimeException ex) {

		String message = ex.getMessage();
		ErrorResponse<ErrorCode> response = ErrorResponse.<ErrorCode>builder()
			.errorCode(UNKNOWN_ERROR)
			.msg(message)
			.build();

		return ResponseEntity.internalServerError().body(response);
	}
	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse<ErrorCode>> handleBindException(BindException ex) {

		BindingResult bindingResult = ex.getBindingResult();
		List<FieldError> errors = bindingResult.getFieldErrors();
		String errorMessage = errors.stream()
			.map(FieldError::getDefaultMessage)
			.collect(Collectors.joining("\n"));

		ErrorResponse<ErrorCode> response = ErrorResponse.<ErrorCode>builder()
			.errorCode(NOT_VALID_PARAM)
			.msg(errorMessage)
			.build();

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse<ErrorCode>> handleNoResourceFoundException(NoResourceFoundException ex) {
		String resourcePath = ex.getResourcePath();
		String httpMethod = ex.getHttpMethod().toString();

		ErrorResponse<ErrorCode> response = ErrorResponse.<ErrorCode>builder()
			.errorCode(NO_FOUND_RESOURCE)
			.msg(String.format("No Found Resource [%1$s] /%2$s", httpMethod, resourcePath))
			.build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
