package org.jvalue.ods.filter.reference;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.TimeUnit;

public class FilterChainExecutionInterval {

	private final long period;
	private final TimeUnit unit;

	@JsonCreator
	public FilterChainExecutionInterval(
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

}
