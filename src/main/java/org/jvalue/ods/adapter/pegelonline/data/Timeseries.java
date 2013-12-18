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

package org.jvalue.ods.adapter.pegelonline.data;

/**
 * The Class Timeseries.
 */

public class Timeseries {

	/** The comment. */

	private Comment comment;

	private CurrentMeasurement currentMeasurement;

	/** The equidistance. */
	private Number equidistance;

	/** The gauge zero. */

	private GaugeZero gaugeZero;

	/** The longname. */

	private String longname;

	/** The shortname. */
	private String shortname;

	/** The unit. */
	private String unit;

	/**
	 * Gets the comment.
	 * 
	 * @return the comment
	 */
	public Comment getComment() {
		return this.comment;
	}

	/**
	 * Sets the comment.
	 * 
	 * @param comment
	 *            the new comment
	 */
	public void setComment(Comment comment) {
		this.comment = comment;
	}

	/**
	 * Gets the current measurement.
	 * 
	 * @return the current measurement
	 */
	public CurrentMeasurement getCurrentMeasurement() {
		return this.currentMeasurement;
	}

	/**
	 * Sets the current measurement.
	 * 
	 * @param currentMeasurement
	 *            the new current measurement
	 */
	public void setCurrentMeasurement(CurrentMeasurement currentMeasurement) {
		this.currentMeasurement = currentMeasurement;
	}

	/**
	 * Gets the equidistance.
	 * 
	 * @return the equidistance
	 */
	public Number getEquidistance() {
		return this.equidistance;
	}

	/**
	 * Sets the equidistance.
	 * 
	 * @param equidistance
	 *            the new equidistance
	 */
	public void setEquidistance(Number equidistance) {
		this.equidistance = equidistance;
	}

	/**
	 * Gets the gauge zero.
	 * 
	 * @return the gauge zero
	 */
	public GaugeZero getGaugeZero() {
		return this.gaugeZero;
	}

	/**
	 * Sets the gauge zero.
	 * 
	 * @param gaugeZero
	 *            the new gauge zero
	 */
	public void setGaugeZero(GaugeZero gaugeZero) {
		this.gaugeZero = gaugeZero;
	}

	/**
	 * Gets the longname.
	 * 
	 * @return the longname
	 */
	public String getLongname() {
		return this.longname;
	}

	/**
	 * Sets the longname.
	 * 
	 * @param longname
	 *            the new longname
	 */
	public void setLongname(String longname) {
		this.longname = longname;
	}

	/**
	 * Gets the shortname.
	 * 
	 * @return the shortname
	 */
	public String getShortname() {
		return this.shortname;
	}

	/**
	 * Sets the shortname.
	 * 
	 * @param shortname
	 *            the new shortname
	 */
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	/**
	 * Gets the unit.
	 * 
	 * @return the unit
	 */
	public String getUnit() {
		return this.unit;
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
}
