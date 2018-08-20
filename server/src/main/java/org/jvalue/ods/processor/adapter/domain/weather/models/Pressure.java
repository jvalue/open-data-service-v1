package org.jvalue.ods.processor.adapter.domain.weather.models;

import java.text.DecimalFormat;
import java.util.Objects;

public class Pressure {

	private final double value;
	private final PressureType type;


	public Pressure(double value, PressureType type) {
		this.value = Double.valueOf(new DecimalFormat("#.##").format(value));
		this.type = type;
	}


	public double getValue() {
		return value;
	}


	public PressureType getType() {
		return type;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Pressure pressure = (Pressure) o;
		return Double.compare(pressure.value, value) == 0 &&
			type == pressure.type;
	}


	@Override
	public int hashCode() {
		return Objects.hash(value, type);
	}
}
