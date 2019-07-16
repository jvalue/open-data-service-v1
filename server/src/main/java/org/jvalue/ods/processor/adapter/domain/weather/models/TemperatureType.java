/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models;

public enum TemperatureType {
	CELSIUS,
	FAHRENHEIT,
	KELVIN;

	public double toKelvin(double value) {
		double kelvin;
		switch (this) {
			case CELSIUS:
				kelvin = value + 273.15;
				break;
			case FAHRENHEIT:
				kelvin = (value + 459.67 ) * 0.55555555555;
				break;
			default:
				kelvin = value;
		}

		return kelvin;
	}


	public double fromKelvin(double value) {
		double result;
		switch (this) {
			case CELSIUS:
				result = value - 273.15;
				break;
			case FAHRENHEIT:
				result = value * 1.8 - 459.67;
				break;
			default:
				result = value;
		}

		return result;
	}
}
