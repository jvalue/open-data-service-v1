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
package org.jvalue.ods.qa.improvement;

import org.jvalue.ods.data.valuetypes.GenericValueType;
import org.jvalue.ods.data.valuetypes.ListComplexValueType;
import org.jvalue.ods.data.valuetypes.MapComplexValueType;
import org.jvalue.ods.data.valuetypes.SimpleValueType;
import org.jvalue.ods.filter.Filter;
import org.jvalue.ods.utils.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class RenameSourceFilter extends Filter<Object, Object> {

	private MapComplexValueType sourceStructure;
	private MapComplexValueType destinationStructure;
	private String newName;

	public RenameSourceFilter(MapComplexValueType sourceStructure,
			MapComplexValueType destinationStructure, String newName) {
		this.sourceStructure = sourceStructure;
		this.destinationStructure = destinationStructure;
		this.newName = newName;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Object doFilter(Object data) {

		List<Object> improvedObjects = new LinkedList<Object>();

		List<Object> oldObjects = null;
		try {
			oldObjects = (List<Object>) data;
		} catch (Exception e) {
			Log.error("Cannot cast data to list, aborting RenameSourceFilter");
			return data;
		}

		for (Object o : oldObjects) {
			Map<String, Object> map = new HashMap<String, Object>();
			traverseSchema(sourceStructure, newName, o, map);
			insertRenamedValue(o, map, destinationStructure);

			Map<String, Object> finalMo = (Map<String, Object>) o;

			finalMo.put("dataQualityStatus", "improved");

			try {
				finalMo.remove("_id");
				finalMo.remove("_rev");
			} catch (Exception e) {
				Log.error(e.getMessage());
			}

			improvedObjects.add(o);
		}

		return data;
	}

	@SuppressWarnings("unchecked")
	private void traverseSchema(GenericValueType sourceStructure,
			String newName, Object object, Map<String, Object> map) {

		if (sourceStructure instanceof MapComplexValueType) {

			for (Entry<String, GenericValueType> e : ((MapComplexValueType) sourceStructure)
					.getMap().entrySet()) {

				if (e.getValue() instanceof SimpleValueType) {
					map.put(newName,
							((Map<String, Object>) object).remove(e.getKey()));

				} else {
					if (e.getValue() instanceof MapComplexValueType) {
						MapComplexValueType mcvt = (MapComplexValueType) e
								.getValue();
						if (mcvt.getMap() != null) {
							traverseSchema(e.getValue(), newName,
									((Map<String, Object>) object).get(e
											.getKey()), map);
						} else {
							map.put(newName, ((Map<String, Object>) object)
									.remove(e.getKey()));
						}
					}
				}
			}
		} else if (sourceStructure instanceof ListComplexValueType) {
			for (Object o : ((List<Object>) object)) {
				traverseSchema(((ListComplexValueType) sourceStructure)
						.getList().get(0), newName, o, map);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void insertRenamedValue(Object object, Map<String, Object> map,
			GenericValueType destinationStructure) {

		if (destinationStructure instanceof MapComplexValueType) {

			for (Entry<String, GenericValueType> e : ((MapComplexValueType) destinationStructure)
					.getMap().entrySet()) {

				if (e.getValue() == null) {

					if (object instanceof Map) {
						Object ser = map.get(e.getKey());

						// check for correct value here
						((Map<String, Object>) object).put(e.getKey(), ser);
					} else {
						String errmsg = "Invalid renamedSchema.";
						Log.error(errmsg);
						System.err.println(errmsg);
						throw new RuntimeException(errmsg);
					}

				} else {

					if (((Map<String, Object>) object).get(e.getKey()) == null) {
						((Map<String, Object>) object).put(e.getKey(),
								new HashMap<String, Object>());
					}

					insertRenamedValue(
							((Map<String, Object>) object).get(e.getKey()),
							map, e.getValue());
				}
			}
		} else if (destinationStructure instanceof ListComplexValueType) {

			if (!(object instanceof List)) {
				String errmsg = "Invalid renameSchema.";
				Log.error(errmsg);
				System.err.println(errmsg);
				throw new RuntimeException(errmsg);
			}

			for (Object gv : ((List<Object>) object)) {

				insertRenamedValue(gv, map,
						((ListComplexValueType) destinationStructure).getList()
								.get(0));
			}

		}

	}

}
