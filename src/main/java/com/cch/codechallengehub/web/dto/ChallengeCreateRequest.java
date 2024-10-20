package com.cch.codechallengehub.web.dto;

import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.constants.RecruitType;
import com.cch.codechallengehub.web.validation.ValidEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeCreateRequest {

	@NotBlank(message = "challenge name must not be blank")
	private String challengeName;

	@ValidEnum(enumClass = ChallengeLevel.class, message = "challenge level is not valid")
	private String level;
	private String challengeDesc;

	@NotNull(message = "challenge start date must not be null")
	private LocalDateTime startDate;

	@NotNull(message = "challenge end date must not be null")
	private LocalDateTime endDate;

	@ValidEnum(enumClass = RecruitType.class, message = "recruit type is not valid")
	private String recruitType;

	@NotNull(message = "recruit start date must not be null")
	private LocalDateTime recruitStartDate;

	@NotNull(message = "recruit start date must not be null")
	private LocalDateTime recruitEndDate;

	@NotNull(message = "recruit number must not be null")
	@Min(value = 1, message = "recruit number must be greater than 0")
	private Integer recruitNumber;

	@NotEmpty(message = "function requirements must not be empty")
	private List<String> funcRequirements;

	@NotEmpty(message = "challenge tech stacks must not be empty")
	private List<String> techStacks = new ArrayList<>();

	@Valid
	@NotEmpty(message = "challenge quests must not be empty")
	private List<QuestCreateRequest> quests = new ArrayList<>();

}
