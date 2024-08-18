package com.cch.codechallengehub.dto;


import com.cch.codechallengehub.constants.QuestResultType;
import com.cch.codechallengehub.domain.ChallengeQuest;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeQuestCreateDto {

	private String questName;
	private LocalDateTime deadline;
	private String questDetail;
	private QuestResultType resultType;
	private Integer orders;

	public ChallengeQuest toEntity() {
		return ChallengeQuest.builder()
			.questName(questName)
			.deadline(deadline)
			.questDetail(questDetail)
			.resultType(resultType)
			.orders(orders)
			.build();
	}

}
