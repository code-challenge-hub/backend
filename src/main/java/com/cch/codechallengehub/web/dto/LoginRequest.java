package com.cch.codechallengehub.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

	@NotBlank(message = "Email은 필수 입니다.")
	private String email;

	@NotBlank(message = "패스워드는 필수 입니다.")
	private String password;

}
