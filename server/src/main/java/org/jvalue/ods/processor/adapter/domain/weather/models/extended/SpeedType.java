/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

public enum SpeedType {
	METER_PER_SECOND("m / s"),
	KILOMETER_PER_HOUR("km / h");

	private final String type;

	SpeedType(String type) {
		this.type = type;
	}

	public String toString() {
		return type;
	}
}
