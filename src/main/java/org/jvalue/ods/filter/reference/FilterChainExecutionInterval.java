package org.jvalue.ods.filter.reference;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import org.jvalue.ods.utils.Assert;

import java.util.concurrent.TimeUnit;

public class FilterChainExecutionInterval {

	private final long period;
	private final TimeUnit unit;

	@JsonCreator
	public FilterChainExecutionInterval(
			@JsonProperty("period") long period,
			@JsonProperty("unit") TimeUnit unit) {

		Assert.assertNotNull(unit);
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
		if (other == null || !(other instanceof FilterChainExecutionInterval)) return false;
		if (other == this) return true;
		FilterChainExecutionInterval interval = (FilterChainExecutionInterval) other;
		return Objects.equal(period, interval.period)
				&& Objects.equal(unit, interval.unit);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(period, unit);
	}

}
