package com.cch.codechallengehub.api.exception;

import org.springframework.web.bind.annotation.GetMapping;
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

}
