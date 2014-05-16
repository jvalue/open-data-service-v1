package org.jvalue.ods.qa.valueTypes;

import org.jvalue.numbers.NumberType;
import org.jvalue.numbers.Range;

public class Coordinate {

	private final Double latitude;
	private final Double longitude;

	private static NumberType<Double> latitudeType = new NumberType<Double>(
			new Range<Double>(-90.0, 90.0));
	private static NumberType<Double> longitudeType = new NumberType<Double>(
			new Range<Double>(-180.0, 180.0));

	public Coordinate(Double latitude, Double longitude) {
		super();
		this.latitude = latitudeType.validate(latitude);
		this.longitude = longitudeType.validate(longitude);
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

}
