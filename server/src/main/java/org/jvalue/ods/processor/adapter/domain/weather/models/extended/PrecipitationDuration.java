package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.DecimalFormat;
import java.util.Objects;

public class PrecipitationDuration {
	private final double value;
	private final DurationType type;

	@JsonCreator
	public PrecipitationDuration(
		@JsonProperty("value") double value,
		@JsonProperty("type") DurationType type) {
		this.value = Double.valueOf(new DecimalFormat("#.##").format(value));
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
		PrecipitationDuration precipitationDuration = (PrecipitationDuration) o;
		return Double.compare(precipitationDuration.value, value) == 0 &&
			type == precipitationDuration.type;
	}


	@Override
	public int hashCode() {
		return Objects.hash(value, type);
	}
}
