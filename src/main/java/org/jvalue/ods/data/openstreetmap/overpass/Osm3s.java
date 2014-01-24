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
 * The Class Osm3s.
 */
public class Osm3s {

	/** The copyright. */
	private String copyright;

	/** The timestamp_osm_base. */
	private String timestamp_osm_base;

	/**
	 * Gets the copyright.
	 * 
	 * @return the copyright
	 */
	public String getCopyright() {
		return this.copyright;
	}

	/**
	 * Sets the copyright.
	 * 
	 * @param copyright
	 *            the new copyright
	 */
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	/**
	 * Gets the timestamp_osm_base.
	 * 
	 * @return the timestamp_osm_base
	 */
	public String getTimestamp_osm_base() {
		return this.timestamp_osm_base;
	}

	/**
	 * Sets the timestamp_osm_base.
	 * 
	 * @param timestamp_osm_base
	 *            the new timestamp_osm_base
	 */
	public void setTimestamp_osm_base(String timestamp_osm_base) {
		this.timestamp_osm_base = timestamp_osm_base;
	}
}
