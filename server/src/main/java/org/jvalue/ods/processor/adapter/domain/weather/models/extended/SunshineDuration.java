/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class SunshineDuration {
	private final double value;
	private final DurationType type;

	@JsonCreator
	public SunshineDuration(
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
		SunshineDuration sunshineDuration = (SunshineDuration) o;
		return Double.compare(sunshineDuration.value, value) == 0 &&
			type == sunshineDuration.type;
	}


	@Override
	public int hashCode() {
		return Objects.hash(value, type);
	}
}
