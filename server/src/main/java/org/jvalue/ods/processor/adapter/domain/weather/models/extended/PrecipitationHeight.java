/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PrecipitationHeight {
	private final double value;
	private final LengthType type;

	@JsonCreator
	public PrecipitationHeight(
		@JsonProperty("value") double value,
		@JsonProperty("type") LengthType type) {
		this.value = value;
		this.type = type;
	}


	public double getValue() {
		return value;
	}


	public LengthType getType() {
		return type;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PrecipitationHeight precipitationHeight = (PrecipitationHeight) o;
		return Double.compare(precipitationHeight.value, value) == 0 &&
			type == precipitationHeight.type;
	}


	@Override
	public int hashCode() {
		return Objects.hash(value, type);
	}
}
