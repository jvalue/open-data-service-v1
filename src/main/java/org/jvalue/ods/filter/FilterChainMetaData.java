package org.jvalue.ods.filter;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FilterChainMetaData {

	private final long executionPeriod;

	@JsonCreator
	public FilterChainMetaData(
			@JsonProperty("executionPeriod") long executionPeriod) {

		this.executionPeriod = executionPeriod;
	}


	public long getExecutionPeriod() {
		return executionPeriod;
	}

}
