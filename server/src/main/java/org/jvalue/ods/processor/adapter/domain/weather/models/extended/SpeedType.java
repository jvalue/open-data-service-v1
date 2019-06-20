package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

public enum SpeedType {
	METER_PER_SECOND("km / h"),
	KILOMETER_PER_HOUR("m / s");

	private final String type;

	SpeedType(String type) {
		this.type = type;
	}

	public String toString() {
		return type;
	}
}
