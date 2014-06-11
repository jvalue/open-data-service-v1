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

	private static Map<String, GenericValueType> map = new HashMap<String, GenericValueType>();
	static {
		map.put("Coordinate", new SimpleValueType("Coordinate", ObjectTypeEnum.Reference));
		map.put("java.lang.String", new SimpleValueType("java.lang.String", ObjectTypeEnum.Domain));
		map.put("java.lang.Number", new SimpleValueType("java.lang.Number", ObjectTypeEnum.Domain));
		map.put("java.lang.Boolean", new SimpleValueType("java.lang.Boolean", ObjectTypeEnum.Domain));
		map.put("Null", new SimpleValueType("Null", ObjectTypeEnum.Domain));
	}


	public static final GenericValueType 
		VALUETYPE_STRING = getGenericValueType("java.lang.String"),
		VALUETYPE_NUMBER = getGenericValueType("java.lang.Number"),
		VALUETYPE_BOOLEAN = getGenericValueType("java.lang.Boolean"),
		VALUETYPE_NULL = getGenericValueType("Null");



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

}
