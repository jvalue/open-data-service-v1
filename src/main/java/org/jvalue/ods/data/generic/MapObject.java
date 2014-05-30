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
package org.jvalue.ods.data.generic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The Class MapValue.
 */
@JsonInclude(Include.NON_NULL)
public class MapObject extends GenericEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7476268653001753943L;
	
	/** The map. */
	private Map<String, Serializable> map = new HashMap<String, Serializable>();

	/**
	 * Instantiates a new map value.
	 */
	public MapObject() {

	}

	/**
	 * Gets the map.
	 * 
	 * @return the map
	 */
	@JsonValue
	public Map<String, Serializable> getMap() {
		return map;
	}

}
