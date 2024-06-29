package com.cch.codechallengehub.web.exception;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/exception")
	public void exception() throws Exception {
		throw new Exception("Exception!");
	}

	@GetMapping("/runtime-exception")
	public void runtimeException() {
		throw new RuntimeException("RuntimeException!");
	}

	@GetMapping("/handle-missing-servlet-request-parameter")
	public void servletRequestBindingException(@RequestParam(required = true) int num) {
	}

	@PostMapping("/method-argument-not-valid-exception")
	public void methodArgumentNotValidException(@Valid @RequestBody TestDto testDto) {
	}

	@GetMapping("/handler-method-validation-exception")
	public void handlerMethodValidationException(@RequestParam(name = "num") @Min(1) int num) {
	}

	@GetMapping("/type-mismatch")
	public void typeMismatch(@RequestParam(name = "num") int num) {
	}

}
