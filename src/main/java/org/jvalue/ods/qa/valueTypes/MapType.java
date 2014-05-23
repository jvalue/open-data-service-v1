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

import java.util.Map;
import java.util.Map.Entry;

import org.jvalue.ValueType;

/**
 * The Class MapType.
 * 
 * @param <E>
 *            the element type
 */
public class MapType extends ValueType<Map<String, ValueType<?>>> {

	/** The type map. */
	private Map<String, ValueType<?>> typeMap;

	/**
	 * Instantiates a new map type.
	 * 
	 * @param typeMap
	 *            the type map
	 */
	public MapType(Map<String, ValueType<?>> typeMap) {
		this.typeMap = typeMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ValueType#isValidInstance(java.lang.Object)
	 */
	@Override
	public boolean isValidInstance(Map<String, ValueType<?>> map) {

		for (Entry<String, ValueType<?>> e : map.entrySet()) {

			ValueType<?> k = typeMap.get(e.getKey());

			if (k == null) {
				continue;
				// if additional value is ok
			}

			if (!k.isValidInstance(e.getValue())) {
				return false;
			}
		}

		return true;
	}

}