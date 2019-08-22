/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class WindSpeed {
	private final double value;
	private final SpeedType type;

	@JsonCreator
	public WindSpeed(
		@JsonProperty("value") double value,
		@JsonProperty("type") SpeedType type) {
		this.value = value;
		this.type = type;
	}


	public double getValue() {
		return value;
	}


	public SpeedType getType() {
		return type;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WindSpeed windSpeed = (WindSpeed) o;
		return Double.compare(windSpeed.value, value) == 0 &&
			type == windSpeed.type;
	}


	@Override
	public int hashCode() {
		return Objects.hash(value, type);
	}
}
