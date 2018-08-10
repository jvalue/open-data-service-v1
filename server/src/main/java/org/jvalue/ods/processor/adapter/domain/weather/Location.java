package org.jvalue.ods.processor.adapter.domain.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Location implements Serializable {

	private  String city;
	private  String zipCode;
	private  Coordinate coordinate;

	Location(
		@JsonProperty("city") String city,
		@JsonProperty("zipCode") String zipCode,
		@JsonProperty("coordinate") Coordinate coordinate) {
		this.city = city;
		this.zipCode = zipCode;
		this.coordinate = coordinate;
	}


	public boolean hasCity() {
		return isNotEmpty(city);
	}


	public String getCity() {
		return city;
	}


	public boolean hasZipCode() {
		return isNotEmpty(zipCode);
	}


	public String getZipCode() {
		return zipCode;
	}


	public boolean hasCoordinate() {
		return coordinate != null;
	}


	public Coordinate getCoordinate() {
		return coordinate;
	}


	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

}
