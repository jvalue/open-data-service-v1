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

import java.util.Map;
import java.util.Map.Entry;

import org.jvalue.ods.data.generic.GenericValue;
import org.jvalue.ods.data.generic.ListValue;
import org.jvalue.ods.data.generic.MapValue;
import org.jvalue.ods.data.schema.BoolSchema;
import org.jvalue.ods.data.schema.ListSchema;
import org.jvalue.ods.data.schema.MapSchema;
import org.jvalue.ods.data.schema.NumberSchema;
import org.jvalue.ods.data.schema.Schema;
import org.jvalue.ods.data.schema.StringSchema;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.schema.SchemaManager;

/**
 * The Class CombineFilter.
 */
public class CombineFilter implements OdsFilter {

	/** The data. */
	protected MapValue data;

	/** The schema. */
	private MapSchema schema;

	private MapSchema combinedSchema;

	/**
	 * Instantiates a new combine filter.
	 * 
	 * @param data
	 *            the data
	 * @param schema
	 *            the schema
	 * @param combinedName
	 *            the combined name
	 */
	public CombineFilter(MapValue data, MapSchema schema,
			MapSchema combinedSchema) {
		this.data = data;
		this.schema = schema;
		this.combinedSchema = combinedSchema;
	}

	/**
	 * Filter.
	 * 
	 * @return the map value
	 */
	public MapValue filter() {

		if (!SchemaManager.validateGenericValusFitsSchema(data, schema)) {
			Logging.info(this.getClass(),
					"Could not validate schema in CombineFilter.");
			return data;
		}

		MapValue mv = new MapValue();

		traverseSchema(schema, data, mv.getMap());

		insertCombinedValue(data, mv, combinedSchema);

		return data;
	}

	/**
	 * Traverse schema.
	 * 
	 * @param sourceStructure
	 *            the schema
	 * @param data
	 *            the data
	 * @param combinedMap
	 */
	private void traverseSchema(Schema sourceStructure, GenericValue data,
			Map<String, GenericValue> combinedMap) {

		if (sourceStructure instanceof MapSchema) {

			for (Entry<String, Schema> e : ((MapSchema) sourceStructure).getMap()
					.entrySet()) {

				if (e.getValue() instanceof NumberSchema
						|| e.getValue() instanceof StringSchema
						|| e.getValue() instanceof BoolSchema) {

					combinedMap.put(e.getKey(), ((MapValue) data).getMap()
							.remove(e.getKey()));

				} else {
					traverseSchema(e.getValue(), ((MapValue) data).getMap()
							.get(e.getKey()), combinedMap);
				}
			}
		} else if (sourceStructure instanceof ListSchema) {

			for (GenericValue gv : ((ListValue) data).getList()) {

				traverseSchema(((ListSchema) sourceStructure).getList().get(0), gv,
						combinedMap);

			}

		}
	}

	private void insertCombinedValue(GenericValue data, MapValue mv,
			Schema destinationStructure) {

		if (destinationStructure instanceof MapSchema) {

			for (Entry<String, Schema> e : ((MapSchema) destinationStructure)
					.getMap().entrySet()) {

				if (e.getValue() == null) {

					if (data instanceof MapValue) {
						((MapValue) data).getMap().put(e.getKey(), mv);
					} else {
						String errmsg = "Invalid combinedSchema.";
						Logging.error(this.getClass(), errmsg);
						System.err.println(errmsg);
						throw new RuntimeException(errmsg);
					}

				} else {

					if (((MapValue) data).getMap().get(e.getKey()) == null) {
						((MapValue) data).getMap().put(e.getKey(),
								new MapValue());
					}

					insertCombinedValue(
							((MapValue) data).getMap().get(e.getKey()), mv,
							e.getValue());
				}
			}
		} else if (destinationStructure instanceof ListSchema) {

			if (!(data instanceof ListValue)) {
				String errmsg = "Invalid combinedSchema.";
				Logging.error(this.getClass(), errmsg);
				System.err.println(errmsg);
				throw new RuntimeException(errmsg);
			}

			for (GenericValue gv : ((ListValue) data).getList()) {

				insertCombinedValue(gv, mv, ((ListSchema) destinationStructure)
						.getList().get(0));
			}

		}

	}
}
