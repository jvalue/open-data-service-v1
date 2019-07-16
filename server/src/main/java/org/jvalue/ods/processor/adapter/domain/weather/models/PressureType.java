/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models;

public enum PressureType {
	PA("Pa"),
	H_PA("hPa"),
	M_BAR("mbar");

	private final String type;

	PressureType(String type) {
		this.type = type;
	}


	public String toString() {
		return type;
	}
}
