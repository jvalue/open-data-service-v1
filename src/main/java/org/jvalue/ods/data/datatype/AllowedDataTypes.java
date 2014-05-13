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
package org.jvalue.ods.data.datatype;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class AllowedDataTypes.
 */
public class AllowedDataTypes {

	/** The Constant dataTypeMap. */
	private final static Map<String, DataType> dataTypeMap = createMap();
	
	/**
	 * Gets the data type.
	 *
	 * @param name the name
	 * @return the data type
	 */
	public static DataType getDataType(String name)
	{
		return dataTypeMap.get(name);		
	}

	/**
	 * Creates the map.
	 *
	 * @return the map
	 */
	private static Map<String, DataType> createMap() {
		Map<String, DataType> result = new HashMap<String, DataType>();
		result.put("Coordinate", new DataType("Coordinate", DataTypeEnum.Domain));
        
        result.put("String", new DataType("String", DataTypeEnum.Builtin));
        result.put("Number", new DataType("Number", DataTypeEnum.Builtin));
        result.put("Bool", new DataType("Bool", DataTypeEnum.Builtin));
        
        return Collections.unmodifiableMap(result);
	}
	
}
