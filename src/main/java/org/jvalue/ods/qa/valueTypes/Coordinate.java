/*  Open Data Service
    Copyright (C) 2013  Tsysin Konstantin, Reischl Patrick

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
 */
package org.jvalue.ods.qa.valueTypes;

import org.jvalue.numbers.NumberType;
import org.jvalue.numbers.Range;

/**
 * The Class Coordinate.
 */
public class Coordinate {

	/** The latitude. */
	private final Double latitude;

	/** The longitude. */
	private final Double longitude;

	/** The latitude type. */
	private static NumberType<Double> latitudeType = new NumberType<Double>(
			new Range<Double>(-90.0, 90.0));

	/** The longitude type. */
	private static NumberType<Double> longitudeType = new NumberType<Double>(
			new Range<Double>(-180.0, 180.0));

	/**
	 * Instantiates a new coordinate.
	 * 
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 */
	public Coordinate(Double latitude, Double longitude) {
		super();
		this.latitude = latitudeType.validate(latitude);
		this.longitude = longitudeType.validate(longitude);
	}

	/**
	 * Gets the latitude.
	 * 
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Gets the longitude.
	 * 
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

}
