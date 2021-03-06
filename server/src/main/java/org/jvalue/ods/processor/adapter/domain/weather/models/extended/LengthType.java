/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

public enum LengthType {
	KILOMETER("km"),
	METER("m"),
	DECIMETER("dm"),
	CENTIMETER("cm"),
	MILLIMETER("mm");

	private final String type;

	LengthType(String type) {
		this.type = type;
	}

	public String toString() {
		return type;
	}
}
