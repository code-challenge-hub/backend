package com.cch.codechallengehub.api.exception;


import jakarta.validation.constraints.NotNull;

public class TestDto {

	@NotNull(message = "ID는 필수 입니다.")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
