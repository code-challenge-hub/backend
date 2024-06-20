package com.cch.codechallengehub.web.exception;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Order(1)
public class MvcExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
		MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status,
		WebRequest request) {

		String errorMessage = ex.getMessage();
		ErrorResponse<ErrorCode> response = getBadRequestErrorResponse(errorMessage);

		return ResponseEntity.badRequest().body(response);
	}

	// 객체 파라미터 유효성 검증 실패
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
		WebRequest request) {

		BindingResult bindingResult = ex.getBindingResult();
		List<FieldError> errors = bindingResult.getFieldErrors();
		String errorMessage = errors.stream()
			.map(FieldError::getDefaultMessage)
			.collect(Collectors.joining("\n"));

		ErrorResponse<ErrorCode> response = getBadRequestErrorResponse(errorMessage);

		return ResponseEntity.badRequest().body(response);
	}

	// 파라미터 유효성 검증 실패
	@Override
	protected ResponseEntity<Object> handleHandlerMethodValidationException(
		HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status,
		WebRequest request) {

		List<ParameterValidationResult> validationResults = ex.getAllValidationResults();
		String errorMessage = getMessageByParameterValidationResults(validationResults);

		ErrorResponse<ErrorCode> response = getBadRequestErrorResponse(errorMessage);

		return ResponseEntity.badRequest().body(response);
	}

	private static String getMessageByParameterValidationResults(List<ParameterValidationResult> validationResults) {
		return validationResults.stream()
			.map(validationResult -> {
				List<MessageSourceResolvable> resolvableErrors = validationResult.getResolvableErrors();
				return resolvableErrors.stream()
					.map(MessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.joining("\n"));
			})
			.collect(Collectors.joining("\n"));
	}

	// 파라미터 타입 에러
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		String errorMessage = ex.getMessage();
		ErrorResponse<ErrorCode> response = getBadRequestErrorResponse(errorMessage);

		return ResponseEntity.badRequest().body(response);
	}

	private static ErrorResponse<ErrorCode> getBadRequestErrorResponse(String errorMessage) {
		return ErrorResponse.<ErrorCode>builder()
			.errorCode(ErrorCode.NOT_VALID_PARAM)
			.msg(errorMessage)
			.build();
	}

	@Override
	protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		String resourcePath = ex.getResourcePath();
		String httpMethod = ex.getHttpMethod().toString();

		ErrorResponse<ErrorCode> response = ErrorResponse.<ErrorCode>builder()
			.errorCode(ErrorCode.NO_FOUND_RESOURCE)
			.msg(String.format("No Found Resource [%1$s] /%2$s", httpMethod, resourcePath))
			.build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

}
