package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

public enum SpeedType {
	METER_PER_SECOND("m / s"),
	KILOMETER_PER_HOUR("km / h");

	private final String type;

	SpeedType(String type) {
		this.type = type;
	}

	public String toString() {
		return type;
	}
}
