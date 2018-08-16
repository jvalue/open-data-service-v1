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
}
