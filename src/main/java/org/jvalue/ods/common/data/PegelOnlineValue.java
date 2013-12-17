/*
    Open Data Service
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
package org.jvalue.ods.common.data;

/**
 * The Class PegelOnlineValue.
 */
public class PegelOnlineValue {

	/** The name. */
	private String name;

	/** The timestamp. */
	private String timestamp;

	/** The value. */
	private double value;

	/** The unit. */
	private String unit;

	// in meters, JValue?
	/** The altitude. */
	private double altitude;

	// in minutes, JValue? time between values
	/** The equidistance. */
	private int equidistance;

	/** The comment. */
	private String comment;

	/**
	 * Instantiates a new pegel online value.
	 * 
	 * @param name
	 *            the name
	 * @param timestamp
	 *            the timestamp
	 * @param value
	 *            the value
	 * @param unit
	 *            the unit
	 * @param altitude
	 *            the altitude
	 * @param equidistance
	 *            the equidistance
	 * @param comment
	 *            the comment
	 */
	public PegelOnlineValue(String name, String timestamp, double value,
			String unit, double altitude, int equidistance, String comment) {
		this.name = name;
		this.timestamp = timestamp;
		this.value = value;
		this.unit = unit;
		this.altitude = altitude;
		this.equidistance = equidistance;
		this.comment = comment;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
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
	 * Gets the timestamp.
	 * 
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 * 
	 * @param timestamp
	 *            the new timestamp
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Gets the unit.
	 * 
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Sets the unit.
	 * 
	 * @param unit
	 *            the new unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * Gets the altitude.
	 * 
	 * @return the altitude
	 */
	public double getAltitude() {
		return altitude;
	}

	/**
	 * Sets the altitude.
	 * 
	 * @param altitude
	 *            the new altitude
	 */
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	/**
	 * Gets the equidistance.
	 * 
	 * @return the equidistance
	 */
	public int getEquidistance() {
		return equidistance;
	}

	/**
	 * Sets the equidistance.
	 * 
	 * @param equidistance
	 *            the new equidistance
	 */
	public void setEquidistance(int equidistance) {
		this.equidistance = equidistance;
	}

	/**
	 * Gets the comment.
	 * 
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the comment.
	 * 
	 * @param comment
	 *            the new comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
}
