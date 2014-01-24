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

import java.util.List;

/**
 * The Class Overpass.
 */
public class Overpass {

	/** The elements. */
	private List<?> elements;

	/** The generator. */
	private String generator;

	/** The osm3s. */
	private Osm3s osm3s;

	/** The version. */
	private Number version;

	/**
	 * Gets the elements.
	 * 
	 * @return the elements
	 */
	public List<?> getElements() {
		return this.elements;
	}

	/**
	 * Sets the elements.
	 * 
	 * @param elements
	 *            the new elements
	 */
	public void setElements(List<?> elements) {
		this.elements = elements;
	}

	/**
	 * Gets the generator.
	 * 
	 * @return the generator
	 */
	public String getGenerator() {
		return this.generator;
	}

	/**
	 * Sets the generator.
	 * 
	 * @param generator
	 *            the new generator
	 */
	public void setGenerator(String generator) {
		this.generator = generator;
	}

	/**
	 * Gets the osm3s.
	 * 
	 * @return the osm3s
	 */
	public Osm3s getOsm3s() {
		return this.osm3s;
	}

	/**
	 * Sets the osm3s.
	 * 
	 * @param osm3s
	 *            the new osm3s
	 */
	public void setOsm3s(Osm3s osm3s) {
		this.osm3s = osm3s;
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public Number getVersion() {
		return this.version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the new version
	 */
	public void setVersion(Number version) {
		this.version = version;
	}
}
