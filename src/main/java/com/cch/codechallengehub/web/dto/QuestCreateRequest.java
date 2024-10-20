package com.cch.codechallengehub.web.dto;

import com.cch.codechallengehub.constants.QuestResultType;
import com.cch.codechallengehub.web.validation.ValidEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestCreateRequest {

	@NotBlank(message = "quest name must be not blank")
	private String questName;

	@NotNull(message = "quest deadline must be not null")
	private LocalDateTime deadline;

	private String questDetail;

	@NotNull(message = "quest result type must be not null")
	@ValidEnum(enumClass = QuestResultType.class, message = "quest result type is not valid")
	private String resultType;

	@NotNull(message = "quest orders must be not null")
	@Min(value = 0, message = "quest orders must greater than or equal to 0")
	private Integer orders;

}
