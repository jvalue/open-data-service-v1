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

import java.util.Collection;

/**
 * The Class PegelOnlineData.
 */
public class PegelOnlineData extends ODSData {

	/** The uuid. */
	private final String uuid;

	/** The number. */
	private int number;

	/** The name. */
	private String name;

	/** The km. */
	private float km;

	/** The longitude. */
	private float longitude;

	/** The latitude. */
	private float latitude;

	/** The watername. */
	private String watername;

	/** The timeseries. */
	private Collection<PegelOnlineValue> timeseries;

	/**
	 * Instantiates a new pegel online data.
	 * 
	 * @param uuid
	 *            the uuid
	 * @param number
	 *            the number
	 * @param name
	 *            the name
	 * @param km
	 *            the km
	 * @param longitude
	 *            the longitude
	 * @param latitude
	 *            the latitude
	 * @param watername
	 *            the watername
	 * @param timeseries
	 *            the timeseries
	 */
	public PegelOnlineData(String uuid, int number, String name, float km,
			float longitude, float latitude, String watername,
			Collection<PegelOnlineValue> timeseries) {
		this.uuid = uuid;
		this.number = number;
		this.name = name;
		this.km = km;
		this.longitude = longitude;
		this.latitude = latitude;
		this.watername = watername;
		this.timeseries = timeseries;
	}

	/**
	 * Gets the uuid.
	 * 
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Gets the number.
	 * 
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Sets the number.
	 * 
	 * @param number
	 *            the new number
	 */
	public void setNumber(int number) {
		this.number = number;
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
	 * Gets the km.
	 * 
	 * @return the km
	 */
	public float getKm() {
		return km;
	}

	/**
	 * Sets the km.
	 * 
	 * @param km
	 *            the new km
	 */
	public void setKm(float km) {
		this.km = km;
	}

	/**
	 * Gets the longitude.
	 * 
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 * 
	 * @param longitude
	 *            the new longitude
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the latitude.
	 * 
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 * 
	 * @param latitude
	 *            the new latitude
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the watername.
	 * 
	 * @return the watername
	 */
	public String getWatername() {
		return watername;
	}

	/**
	 * Sets the watername.
	 * 
	 * @param watername
	 *            the new watername
	 */
	public void setWatername(String watername) {
		this.watername = watername;
	}

	/**
	 * Gets the timeseries.
	 * 
	 * @return the timeseries
	 */
	public Collection<PegelOnlineValue> getTimeseries() {
		return timeseries;
	}

	/**
	 * Sets the timeseries.
	 * 
	 * @param timeseries
	 *            the new timeseries
	 */
	public void setTimeseries(Collection<PegelOnlineValue> timeseries) {
		this.timeseries = timeseries;
	}

}
