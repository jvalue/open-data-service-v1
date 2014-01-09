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

import java.util.List;

import org.ektorp.support.CouchDbDocument;

/**
 * The Class Station.
 */
public class Station extends CouchDbDocument {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The agency. */
	private String agency;

	/** The km. */
	private Number km;

	/** The latitude. */
	private Number latitude;

	/** The longitude. */
	private Number longitude;

	/** The longname. */
	private String longname;

	/** The number. */
	private String number;

	/** The shortname. */
	private String shortname;

	/** The timeseries. */
	private List<Timeseries> timeseries;

	/** The uuid. */
	private String uuid;

	/** The water. */
	private Water water;

	/** The old measurements. */
	private List<Measurement> oldMeasurements;

	/**
	 * Gets the agency.
	 * 
	 * @return the agency
	 */
	public String getAgency() {
		return this.agency;
	}

	/**
	 * Sets the agency.
	 * 
	 * @param agency
	 *            the new agency
	 */
	public void setAgency(String agency) {
		this.agency = agency;
	}

	/**
	 * Gets the km.
	 * 
	 * @return the km
	 */
	public Number getKm() {
		return this.km;
	}

	/**
	 * Sets the km.
	 * 
	 * @param km
	 *            the new km
	 */
	public void setKm(Number km) {
		this.km = km;
	}

	/**
	 * Gets the latitude.
	 * 
	 * @return the latitude
	 */
	public Number getLatitude() {
		return this.latitude;
	}

	/**
	 * Sets the latitude.
	 * 
	 * @param latitude
	 *            the new latitude
	 */
	public void setLatitude(Number latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 * 
	 * @return the longitude
	 */
	public Number getLongitude() {
		return this.longitude;
	}

	/**
	 * Sets the longitude.
	 * 
	 * @param longitude
	 *            the new longitude
	 */
	public void setLongitude(Number longitude) {
		this.longitude = longitude;
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
	 * Gets the number.
	 * 
	 * @return the number
	 */
	public String getNumber() {
		return this.number;
	}

	/**
	 * Sets the number.
	 * 
	 * @param number
	 *            the new number
	 */
	public void setNumber(String number) {
		this.number = number;
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
	 * Gets the timeseries.
	 * 
	 * @return the timeseries
	 */
	public List<Timeseries> getTimeseries() {
		return this.timeseries;
	}

	/**
	 * Sets the timeseries.
	 * 
	 * @param timeseries
	 *            the new timeseries
	 */
	public void setTimeseries(List<Timeseries> timeseries) {
		this.timeseries = timeseries;
	}

	/**
	 * Gets the uuid.
	 * 
	 * @return the uuid
	 */
	public String getUuid() {
		return this.uuid;
	}

	/**
	 * Sets the uuid.
	 * 
	 * @param uuid
	 *            the new uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Gets the water.
	 * 
	 * @return the water
	 */
	public Water getWater() {
		return this.water;
	}

	/**
	 * Sets the water.
	 * 
	 * @param water
	 *            the new water
	 */
	public void setWater(Water water) {
		this.water = water;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		return "uuid: " + uuid + "\n" + "number: " + number + "\n"
				+ "longname: " + longname + "\n" + "km: " + km + "\n"
				+ "agency: " + agency + "\n" + "longitude: " + longitude + "\n"
				+ "latitude: " + latitude + "\n" + "water: " + water + "\n"
				+ "timeseries: " + timeseries + "\n" + "oldMeasurements: "
				+ oldMeasurements;
	}

	/**
	 * Gets the old measurements.
	 *
	 * @return the old measurements
	 */
	public List<Measurement> getOldMeasurements() {
		return oldMeasurements;
	}

	/**
	 * Sets the old measurements.
	 *
	 * @param oldMeasurements the new old measurements
	 */
	public void setOldMeasurements(List<Measurement> oldMeasurements) {
		this.oldMeasurements = oldMeasurements;
	}

}
