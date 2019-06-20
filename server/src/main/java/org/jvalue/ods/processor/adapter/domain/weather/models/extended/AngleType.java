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
