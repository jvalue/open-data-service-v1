package org.jvalue.ods.processor.adapter.domain.weather.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Temperature {

	private final double value;
	private final TemperatureType type;

	public Temperature(double value, TemperatureType type) {
		this.value = round(value);
		this.type = type;
	}


	public double getValue() {
		return value;
	}


	public double getValueInKelvin() {
		double kelvin = type.toKelvin(value);
		return round(kelvin);
	}


	public TemperatureType getType() {
		return type;
	}


	private double round(double rawValue) {
		BigDecimal decimal = new BigDecimal(Double.toString(rawValue));
		decimal = decimal.setScale(2, RoundingMode.HALF_UP);
		return decimal.doubleValue();
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
