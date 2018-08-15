package org.jvalue.ods.processor.adapter.domain.weather.models;

import java.text.DecimalFormat;
import java.util.Objects;

public class Temperature {

	private final double value;
	private final TemperatureType type;

	public Temperature(double value, TemperatureType type) {
		this.value = Double.valueOf(new DecimalFormat("#.##").format(value));
		this.type = type;
	}


	public double getValue() {
		return value;
	}


	public TemperatureType getType() {
		return type;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Temperature that = (Temperature) o;
		return Double.compare(that.value, value) == 0 &&
			type == that.type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, type);
	}
}
