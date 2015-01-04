package org.jvalue.ods.api.processors;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.concurrent.TimeUnit;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ExecutionInterval {

	@Min(1) private final long period;
	@NotNull private final TimeUnit unit;

	@JsonCreator
	public ExecutionInterval(
			@JsonProperty("period") long period,
			@JsonProperty("unit") TimeUnit unit) {

		this.period = period;
		this.unit = unit;
	}


	public long getPeriod() {
		return period;
	}


	public TimeUnit getUnit() {
		return unit;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ExecutionInterval)) return false;
		if (other == this) return true;
		ExecutionInterval interval = (ExecutionInterval) other;
		return Objects.equal(period, interval.period)
				&& Objects.equal(unit, interval.unit);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(period, unit);
	}

}
