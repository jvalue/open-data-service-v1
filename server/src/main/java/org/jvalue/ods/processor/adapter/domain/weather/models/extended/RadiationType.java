/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

/*
SI unit for Radiation is W/m^2.
This does not work with values given in J and kJ. One would have to know the time of measurement and divide the value by the time to get the correct unit.
The DWD does not because they measure for e.g. an hour. The average Radiation * 1 hour results in J/m^2.
 */
public enum RadiationType {
	JOULE_PER_SQUARE_CENTIMETER("J / cm^2"),
	JOULE_PER_SQUARE_METER("J / m^2"),
	KILOJOULE_PER_SQUARE_METER("kJ / m^2");

	private final String type;

	RadiationType(String type) {
		this.type = type;
	}

	public String toString() {
		return type;
	}

	/**
	 * Convert the value in Watt per square meter (W/m^2) for the given time period.
	 * For ERA5 daily data, the accumulation period is 1 hour (3600 seconds).
	 * W/m2 = J/m2 / seconds
	 */
	public double toWattPerSquareMeter(double value, int seconds) {
		if (!isValid(value) || !isValid(seconds)) {
			return Double.NaN;
		}

		double wattM2 = Double.NaN;
		switch (this) {
			case JOULE_PER_SQUARE_CENTIMETER: wattM2 = value * 10000 / seconds; break;
			case JOULE_PER_SQUARE_METER: wattM2 = value / seconds; break;
			case KILOJOULE_PER_SQUARE_METER: wattM2 = value * 1000 / seconds; break;
		}

		return wattM2;
	}

	private boolean isValid(double value) {
		return !Double.isNaN(value) && value > 0.0;
	}

	private boolean isValid(int value) {
		return value > 0;
	}
}
