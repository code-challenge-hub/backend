package com.cch.codechallengehub.domain;

import static org.springframework.util.Assert.notNull;

import com.cch.codechallengehub.constants.RecruitType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Recruit {

	@Column(name = "recruit_type")
	@Enumerated(EnumType.STRING)
	private RecruitType type;

	@AttributeOverrides({
		@AttributeOverride(name = "startDate", column = @Column(name = "recruit_start_date")),
		@AttributeOverride(name = "endDate", column = @Column(name = "recruit_end_date"))
	})
	@Column(name = "recruit_start_date")
	private Period period;

	@Column(name = "recruit_number")
	private Integer number;

	@Builder
	public Recruit(RecruitType type, Period period, Integer number) {
		setType(type);
		setPeriod(period);
		setNumber(number);
	}


	private void setType(RecruitType recruitType) {
		notNull(recruitType, "Challenge recruit type is not null");
		this.type = recruitType;
	}

	private void setPeriod(Period period) {
		notNull(period, "Challenge recruit period is not null");
		this.period = period;
	}

	private void setNumber(Integer recruitNumber) {
		notNull(recruitNumber, "Challenge recruit number is not null");
		this.number = recruitNumber;
	}

}
