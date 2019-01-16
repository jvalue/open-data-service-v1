/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Temperature {

	private final double value;
	private final TemperatureType type;

	@JsonCreator
	public Temperature(
		@JsonProperty("value") double value,
		@JsonProperty("type") TemperatureType type) {
		this.value = round(value);
		this.type = type;
	}


	public static Temperature fromKelvin(double value, TemperatureType type) {
		return new Temperature(round(type.fromKelvin(value)), type);
	}


	public double getValue() {
		return value;
	}


	@JsonIgnore
	public double getValueInKelvin() {
		double kelvin = type.toKelvin(value);
		return round(kelvin);
	}


	public TemperatureType getType() {
		return type;
	}


	private static double round(double rawValue) {
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
