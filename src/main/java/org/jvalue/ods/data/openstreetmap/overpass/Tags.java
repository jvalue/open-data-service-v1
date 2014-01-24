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

package org.jvalue.ods.data.openstreetmap.overpass;

/**
 * The Class Tags.
 */
public class Tags {

	/** The amenity. */
	private String amenity;

	/** The name. */
	private String name;

	/** The parking. */
	private String parking;

	/**
	 * Gets the amenity.
	 * 
	 * @return the amenity
	 */
	public String getAmenity() {
		return this.amenity;
	}

	/**
	 * Sets the amenity.
	 * 
	 * @param amenity
	 *            the new amenity
	 */
	public void setAmenity(String amenity) {
		this.amenity = amenity;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the parking.
	 * 
	 * @return the parking
	 */
	public String getParking() {
		return this.parking;
	}

	/**
	 * Sets the parking.
	 * 
	 * @param parking
	 *            the new parking
	 */
	public void setParking(String parking) {
		this.parking = parking;
	}
}
