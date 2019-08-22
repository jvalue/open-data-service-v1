/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

public enum AngleType {
    DEGREE("°"),
    RADIANS("°(Rad)");

	private final String type;

	AngleType(String type) {
		this.type = type;
	}

	public String toString() {
		return type;
	}
}
