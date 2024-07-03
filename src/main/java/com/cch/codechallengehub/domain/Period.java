package com.cch.codechallengehub.domain;


import static org.springframework.util.Assert.notNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Period {

	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDateTime endDate;

	public Period(LocalDateTime startDate, LocalDateTime endDate) {
		notNull(startDate, "Start date is not null");
		notNull(endDate, "End date is not null");
		if (startDate.isAfter(endDate)) {
			throw new IllegalStateException("Start date must be before end date");
		}
		this.startDate = startDate;
		this.endDate = endDate;
	}

}
