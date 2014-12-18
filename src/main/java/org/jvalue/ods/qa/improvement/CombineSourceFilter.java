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
import org.jvalue.ods.processor.filter.Filter;
import org.jvalue.ods.utils.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

abstract class CombineSourceFilter implements Filter<Object, Object> {

	private MapComplexValueType sourceStructure;
	private MapComplexValueType destinationStructure;
	
	public CombineSourceFilter(
			MapComplexValueType sourceStructure,
			MapComplexValueType destinationStructure) {
		this.sourceStructure = sourceStructure;
		this.destinationStructure = destinationStructure;
	}

	@SuppressWarnings("unchecked")
	protected Object doProcess(Object data) {

		if (data == null) {
			throw new IllegalArgumentException();
		}

		List<Object> improvedObjects = new LinkedList<Object>();

		List<Object> oldObjects = null;
		try {
			oldObjects = (List<Object>) data;
		} catch (Exception e) {
			Log.error("Cannot cast data to list, aborting CombineSourceFilter");
			return data;
		}
		for (Object gv : oldObjects) {

			Map<String, Object> map = new HashMap<String, Object>();
			traverseSchema(sourceStructure, gv, map);

			insertCombinedValue(gv, map, destinationStructure);

			Map<String, Object> finalMo = (Map<String, Object>) gv;
			finalMo.put("dataQualityStatus", "improved");

			try {
				((Map<String, Object>) gv).remove("_id");
				((Map<String, Object>) gv).remove("_rev");
			} catch (Exception e) {
				Log.error(e.getMessage());
			}
			improvedObjects.add(gv);
		}


		return improvedObjects;
	}


	protected void doOnComplete() {
		// nothing to do here
	}

	@SuppressWarnings("unchecked")
	private void traverseSchema(GenericValueType sourceStructure,
			Object object, Map<String, Object> map) {

		if (sourceStructure instanceof MapComplexValueType) {

			for (Entry<String, GenericValueType> e : ((MapComplexValueType) sourceStructure)
					.getMap().entrySet()) {

				if ((e.getValue() instanceof SimpleValueType)
						&& (((SimpleValueType) e.getValue()).getName() != "Null")) {

					map.put(e.getKey(),
							((Map<String, Object>) object).remove(e.getKey()));

				} else {
					traverseSchema(e.getValue(),
							((Map<String, Object>) object).get(e.getKey()), map);
				}
			}
		} else if (sourceStructure instanceof ListComplexValueType) {

			for (Object gv : ((List<Object>) object)) {

				traverseSchema(((ListComplexValueType) sourceStructure)
						.getList().get(0), gv, map);

			}

		}
	}

	@SuppressWarnings("unchecked")
	private void insertCombinedValue(Object object, Map<String, Object> mv,
			GenericValueType destinationStructure) {

		if (destinationStructure instanceof MapComplexValueType) {

			for (Entry<String, GenericValueType> e : ((MapComplexValueType) destinationStructure)
					.getMap().entrySet()) {

				if (e.getValue() == null) {

					if (object instanceof Map) {

						// check for correct value here
						((Map<String, Object>) object).put(e.getKey(), mv);
					} else {
						String errmsg = "Invalid combinedSchema.";
						Log.error(errmsg);
						System.err.println(errmsg);
						throw new RuntimeException(errmsg);
					}

				} else {

					if (((Map<String, Object>) object).get(e.getKey()) == null) {
						((Map<String, Object>) object).put(e.getKey(),
								new HashMap<String, Object>());
					}

					insertCombinedValue(
							((Map<String, Object>) object).get(e.getKey()), mv,
							e.getValue());
				}
			}
		} else if (destinationStructure instanceof ListComplexValueType) {

			if (!(object instanceof List)) {
				String errmsg = "Invalid combinedSchema.";
				Log.error(errmsg);
				System.err.println(errmsg);
				throw new RuntimeException(errmsg);
			}

			for (Object gv : ((List<Object>) object)) {

				insertCombinedValue(gv, mv,
						((ListComplexValueType) destinationStructure).getList()
								.get(0));
			}

		}

	}
}
