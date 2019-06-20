package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class WindDuration {
	private final double value;
	private final DurationType type;

	@JsonCreator
	public WindDuration(
		@JsonProperty("value") double value,
		@JsonProperty("type") DurationType type) {
		this.value = value;
		this.type = type;
	}


	public double getValue() {
		return value;
	}


	public DurationType getType() {
		return type;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WindDuration windDuration = (WindDuration) o;
		return Double.compare(windDuration.value, value) == 0 &&
			type == windDuration.type;
	}


	@Override
	public int hashCode() {
		return Objects.hash(value, type);
	}
}
