package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

/*
SI unit for Radiation is W/m^2.
This does not work with values given in J and KJ. One would have to know the time of measurement and divide the value by the time to get the correct unit.
The DWD does not because they measure for e.g. an hour. The average Radiation * 1 hour results in J/m^2.
 */
public enum RadiationType {
	JOULE_PER_SQUARE_CENTIMETER("KJ / m^2"),
	KILOJOULE_PER_SQUARE_METER("J / cm^2");

	private final String type;

	RadiationType(String type) {
		this.type = type;
	}

	public String toString() {
		return type;
	}
}
