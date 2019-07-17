package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class WindDirection {
	private final double value;
	private final AngleType type;

	@JsonCreator
	public WindDirection(
		@JsonProperty("value") double value,
		@JsonProperty("type") AngleType type) {
		this.value = value;
		this.type = type;
	}


	public double getValue() {
		return value;
	}


	public AngleType getType() {
		return type;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WindDirection windDirection = (WindDirection) o;
		return Double.compare(windDirection.value, value) == 0 &&
			type == windDirection.type;
	}


	@Override
	public int hashCode() {
		return Objects.hash(value, type);
	}
}
