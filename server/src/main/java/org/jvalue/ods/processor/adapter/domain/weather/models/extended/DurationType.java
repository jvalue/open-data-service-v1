/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

public enum DurationType {
	HOUR("h"),
	MINUTE("min"),
	SECOND("s");

	private final String type;

	DurationType(String type) {
		this.type = type;
	}

	public String toString() {
		return type;
	}
}
