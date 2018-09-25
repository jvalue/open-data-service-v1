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
