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

/**
 * The Class AllowedDataTypes.
 */
public class AllowedValueTypes {

	/** The map. */
	private static Map<String, GenericValueType> map = new HashMap<String, GenericValueType>();

	/**
	 * Gets the data type.
	 * 
	 * @param name
	 *            the name
	 * @return the data type
	 */
	public static GenericValueType getGenericValueType(String name) {
		return map.get(name);
	}

	/**
	 * Adds the base object type.
	 * 
	 * @param name
	 *            the name
	 * @param baseObjectType
	 *            the base object type
	 */
	public static void addBaseObjectType(String name,
			GenericValueType baseObjectType) {

		if (!map.containsKey(name)) {
			map.put(name, baseObjectType);
		}
	}
}
