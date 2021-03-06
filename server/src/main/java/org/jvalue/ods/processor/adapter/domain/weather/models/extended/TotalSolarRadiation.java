/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class TotalSolarRadiation {
	private final double value;
	private final RadiationType type;

	@JsonCreator
	public TotalSolarRadiation(
		@JsonProperty("value") double value,
		@JsonProperty("type") RadiationType type) {
		this.value = value;
		this.type = type;
	}


	public double getValue() {
		return value;
	}


	public RadiationType getType() {
		return type;
	}


	public double toWattPerHourPerSquareMeter() {
		double value = getType().toWattPerSquareMeter(getValue(), 3600);
		return Math.floor(value * 100) / 100;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TotalSolarRadiation totalSolarRadiation = (TotalSolarRadiation) o;
		return Double.compare(totalSolarRadiation.value, value) == 0 &&
			type == totalSolarRadiation.type;
	}


	@Override
	public int hashCode() {
		return Objects.hash(value, type);
	}
}
