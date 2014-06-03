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
package org.jvalue.ods.data.schema;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The Class MapSchema.
 */
@JsonInclude(Include.NON_NULL)
public class MapComplexValueType extends GenericValueType {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1975312956045824117L;
	
	/** The map. */
	private Map<String, GenericValueType> map = new HashMap<String, GenericValueType>();

	/**
	 * Instantiates a new map schema.
	 * 
	 * @param map
	 *            the map
	 */
	public MapComplexValueType(Map<String, GenericValueType> map) {
		this.map = map;
	}

	/**
	 * Gets the map.
	 * 
	 * @return the map
	 */
	@JsonValue
	public Map<String, GenericValueType> getMap() {
		return map;
	}

}
