package org.jvalue.ods.processor.adapter.domain.weather.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Coordinate {

	private final double latitude;
	private final double longitude;

	public Coordinate(
		@JsonProperty("lat") double latitude,
		@JsonProperty("lng") double longitude
		) {

		assertIsValidLatitude(latitude);
		assertIsValidLongitude(longitude);
		this.latitude = latitude;
		this.longitude = longitude;
	}


	public double getLatitude() {
		return latitude;
	}


	public double getLongitude() {
		return longitude;
	}


	@Override
	public String toString() {
		return latitude + ":" + longitude;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Coordinate that = (Coordinate) o;
		return Double.compare(that.latitude, latitude) == 0 &&
			Double.compare(that.longitude, longitude) == 0;
	}


	@Override
	public int hashCode() {
		return Objects.hash(latitude, longitude);
	}


	private void assertIsValidLatitude(double value) {
		if (value < -90 || value > 90) {
			throw new IllegalArgumentException("Latitude value must be in range [-90 .. 90]");
		}
	}


	private void assertIsValidLongitude(double value) {
		if (value < -180 || value > 180) {
			throw new IllegalArgumentException("Longitude value must be in range [-180 .. 180]");
		}
	}
}
