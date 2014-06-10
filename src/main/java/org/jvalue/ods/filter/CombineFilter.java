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
package org.jvalue.ods.filter;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.data.schema.SimpleValueType;
import org.jvalue.ods.data.schema.ListComplexValueType;
import org.jvalue.ods.data.schema.MapComplexValueType;
import org.jvalue.ods.data.schema.GenericValueType;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.schema.SchemaManager;

/**
 * The Class CombineFilter.
 */
public class CombineFilter extends OdsFilter<Void, MapObject> {

	/** The data. */
	protected MapObject data;

	/** The schema. */
	private MapComplexValueType schema;

	/** The combined schema. */
	private MapComplexValueType combinedSchema;

	/**
	 * Instantiates a new combine filter.
	 *
	 * @param data the data
	 * @param schema the schema
	 * @param combinedSchema the combined schema
	 */
	public CombineFilter(MapObject data, MapComplexValueType schema,
			MapComplexValueType combinedSchema) {
		this.data = data;
		this.schema = schema;
		this.combinedSchema = combinedSchema;
	}

	/**
	 * Filter.
	 * 
	 * @return the map value
	 */
	@Override
	protected MapObject filterHelper(Void param) {

		if (!SchemaManager.validateGenericValusFitsSchema(data, schema)) {
			Logging.info(this.getClass(),
					"Could not validate schema in CombineFilter.");
			return data;
		}

		MapObject mv = new MapObject();

		traverseSchema(schema, data, mv.getMap());

		insertCombinedValue(data, mv, combinedSchema);

		return data;
	}

	/**
	 * Traverse schema.
	 *
	 * @param sourceStructure the schema
	 * @param serializable the data
	 * @param map the map
	 */
	private void traverseSchema(GenericValueType sourceStructure, Serializable serializable,
			Map<String, Serializable> map) {

		if (sourceStructure instanceof MapComplexValueType) {

			for (Entry<String, GenericValueType> e : ((MapComplexValueType) sourceStructure)
					.getMap().entrySet()) {

				if ((e.getValue() instanceof SimpleValueType) && (((SimpleValueType)e.getValue()).getName() != "Null")) {
					
					map.put(e.getKey(), ((MapObject) serializable).getMap()
							.remove(e.getKey()));

				} else {
					traverseSchema(e.getValue(), ((MapObject) serializable).getMap()
							.get(e.getKey()), map);
				}
			}
		} else if (sourceStructure instanceof ListComplexValueType) {

			for (Serializable gv : ((ListObject) serializable).getList()) {

				traverseSchema(((ListComplexValueType) sourceStructure).getList().get(0),
						gv, map);

			}

		}
	}

	/**
	 * Insert combined value.
	 *
	 * @param serializable the serializable
	 * @param mv the mv
	 * @param destinationStructure the destination structure
	 */
	private void insertCombinedValue(Serializable serializable, MapObject mv,
			GenericValueType destinationStructure) {

		if (destinationStructure instanceof MapComplexValueType) {

			for (Entry<String, GenericValueType> e : ((MapComplexValueType) destinationStructure)
					.getMap().entrySet()) {

				if (e.getValue() == null) {

					if (serializable instanceof MapObject) {

						// check for correct value here
						((MapObject) serializable).getMap().put(e.getKey(), mv);
					} else {
						String errmsg = "Invalid combinedSchema.";
						Logging.error(this.getClass(), errmsg);
						System.err.println(errmsg);
						throw new RuntimeException(errmsg);
					}

				} else {

					if (((MapObject) serializable).getMap().get(e.getKey()) == null) {
						((MapObject) serializable).getMap().put(e.getKey(),
								new MapObject());
					}

					insertCombinedValue(
							((MapObject) serializable).getMap().get(e.getKey()), mv,
							e.getValue());
				}
			}
		} else if (destinationStructure instanceof ListComplexValueType) {

			if (!(serializable instanceof ListObject)) {
				String errmsg = "Invalid combinedSchema.";
				Logging.error(this.getClass(), errmsg);
				System.err.println(errmsg);
				throw new RuntimeException(errmsg);
			}

			for (Serializable gv : ((ListObject) serializable).getList()) {

				insertCombinedValue(gv, mv, ((ListComplexValueType) destinationStructure)
						.getList().get(0));
			}

		}

	}
}
