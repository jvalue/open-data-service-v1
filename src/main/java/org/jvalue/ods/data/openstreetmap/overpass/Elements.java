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
 * The Class Elements.
 */
public class Elements {

	/** The id. */
	private Number id;

	/** The lat. */
	private Number lat;

	/** The lon. */
	private Number lon;

	/** The tags. */
	private Tags tags;

	/** The type. */
	private String type;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Number getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Number id) {
		this.id = id;
	}

	/**
	 * Gets the lat.
	 * 
	 * @return the lat
	 */
	public Number getLat() {
		return this.lat;
	}

	/**
	 * Sets the lat.
	 * 
	 * @param lat
	 *            the new lat
	 */
	public void setLat(Number lat) {
		this.lat = lat;
	}

	/**
	 * Gets the lon.
	 * 
	 * @return the lon
	 */
	public Number getLon() {
		return this.lon;
	}

	/**
	 * Sets the lon.
	 * 
	 * @param lon
	 *            the new lon
	 */
	public void setLon(Number lon) {
		this.lon = lon;
	}

	/**
	 * Gets the tags.
	 * 
	 * @return the tags
	 */
	public Tags getTags() {
		return this.tags;
	}

	/**
	 * Sets the tags.
	 * 
	 * @param tags
	 *            the new tags
	 */
	public void setTags(Tags tags) {
		this.tags = tags;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
	}
}
