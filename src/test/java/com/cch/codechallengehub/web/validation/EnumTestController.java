package com.cch.codechallengehub.web.validation;

import com.cch.codechallengehub.constants.ChallengeLevel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnumTestController {

	@PostMapping("/parameter-challenge-level")
	public void paramChallengeLevel(
		@ValidEnum(enumClass = ChallengeLevel.class, message = "challenge level is not valid")
		@RequestBody String challengeLevel) {
	}

}
