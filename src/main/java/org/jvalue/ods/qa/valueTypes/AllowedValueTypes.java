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
package org.jvalue.ods.qa.valueTypes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jvalue.ValueType;

/**
 * The Class AllowedDataTypes.
 */
public class AllowedValueTypes {

	/** The Constant dataTypeMap. */
	private final static Map<String, ValueType> valueTypeMap = createMap();

	/**
	 * Gets the data type.
	 * 
	 * @param name
	 *            the name
	 * @return the data type
	 */
	public static ValueType getValueType(String name) {
		return valueTypeMap.get(name);
	}

	/**
	 * Creates the map.
	 * 
	 * @return the map
	 */
	private static Map<String, ValueType> createMap() {
		Map<String, ValueType> result = new HashMap<String, ValueType>();
		//result.put("Coordinate", ...
				
		return Collections.unmodifiableMap(result);
	}

}
