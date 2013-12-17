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

package org.jvalue.ods.adapter.data;

import java.util.List;

/**
 * The Class Water.
 */
public class Water {

	/** The longname. */
	private String longname;

	/** The shortname. */
	private String shortname;

	/** The stations. */
	private List<Station> stations;

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
	 * Gets the stations.
	 * 
	 * @return the stations
	 */
	public List<Station> getStations() {
		return this.stations;
	}

	/**
	 * Sets the stations.
	 * 
	 * @param stations
	 *            the new stations
	 */
	public void setStations(List<Station> stations) {
		this.stations = stations;
	}
}
