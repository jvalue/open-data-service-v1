package org.jvalue.ods.processor.adapter.domain.weather;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Location implements Serializable {

	private static final String DEFAULT_COUNTRY_CODE = "de";
	private final String countryCode;
	private final String city;
	private final String zipCode;
	private final Coordinate coordinate;

	@JsonCreator
	Location(
		@JsonProperty("city") String city,
		@JsonProperty("zipCode") String zipCode,
		@JsonProperty("coordinate") Coordinate coordinate,
		@JsonProperty("countryCode") String countryCode) {
		this.city = city;
		this.zipCode = zipCode;
		this.coordinate = coordinate;
		this.countryCode = countryCode;
	}


	Location(
		@JsonProperty("city") String city,
		@JsonProperty("zipCode") String zipCode,
		@JsonProperty("coordinate") Coordinate coordinate) {
		this.city = city;
		this.zipCode = zipCode;
		this.coordinate = coordinate;
		this.countryCode = DEFAULT_COUNTRY_CODE;
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


	public String getCountryCode() {
		return countryCode;
	}


	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

}
