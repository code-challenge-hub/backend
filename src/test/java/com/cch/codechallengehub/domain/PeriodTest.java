package com.cch.codechallengehub.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;

class PeriodTest {

	@Test
	void create_period_success() {
	    // given
		LocalDateTime startDate = LocalDateTime.now();
		LocalDateTime endDate = startDate.plusDays(1);
		// when
		Period period = new Period(startDate, endDate);
		// then
		assertThat(period.getStartDate()).isEqualTo(startDate);
	}
	@Test
	void create_period_fail_start_date_is_null() {
		// given
		LocalDateTime startDate = null;
		LocalDateTime endDate = LocalDateTime.now().minusDays(1);
		// when
		AbstractThrowableAssert<?, ? extends Throwable> thrownBy = assertThatThrownBy(
			() -> new Period(startDate, endDate));
		// then
		thrownBy.isInstanceOf(IllegalArgumentException.class)
			.message().isEqualTo("Start date is not null");
	}
	@Test
	void create_period_fail_start_date_is_after_end_date() {
		// given
		LocalDateTime startDate = LocalDateTime.now();
		LocalDateTime endDate = startDate.minusDays(1);
		// when
		AbstractThrowableAssert<?, ? extends Throwable> thrownBy = assertThatThrownBy(
			() -> new Period(startDate, endDate));
		// then
		thrownBy.isInstanceOf(IllegalStateException.class)
			.message().isEqualTo("Start date must be before end date");
	}

}