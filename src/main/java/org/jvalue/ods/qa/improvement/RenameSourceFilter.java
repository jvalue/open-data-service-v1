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

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jvalue.ods.data.generic.BaseObject;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.data.valuetypes.GenericValueType;
import org.jvalue.ods.data.valuetypes.ListComplexValueType;
import org.jvalue.ods.data.valuetypes.MapComplexValueType;
import org.jvalue.ods.data.valuetypes.SimpleValueType;
import org.jvalue.ods.filter.Filter;
import org.jvalue.ods.logger.Logging;


public final class RenameSourceFilter implements Filter<GenericEntity, GenericEntity> {

	@Override
	public GenericEntity filter(GenericEntity data) {
		// if (!SchemaManager.validateGenericValusFitsSchema(data, schema)) {
		// Logging.info(this.getClass(),
		// "Could not validate schema in CombineFilter.");
		// return data;
		// }
		// TODO

		MapComplexValueType sourceStructure = createSourceWaterStructure();
		MapComplexValueType destinationStructure = createDestinationWaterStructure();
		String newName = "BodyOfWater";


		List<Serializable> improvedObjects = new LinkedList<Serializable>();
		ListObject oldObjects = (ListObject) data;

		for (Serializable s : oldObjects.getList()) {
			GenericEntity gv = (GenericEntity) s;

			MapObject mv = new MapObject();
			traverseSchema(sourceStructure, newName, gv, mv.getMap());
			insertRenamedValue(gv, mv, destinationStructure);
			
			MapObject finalMo = (MapObject) gv;
			if (!finalMo.getMap().containsKey("dataStatus"))
			{
				finalMo.getMap().put("dataStatus",
					new BaseObject("improved"));
			}

			improvedObjects.add(gv);
		}

		return data;
	}


	private void traverseSchema(GenericValueType sourceStructure, String newName, Serializable serializable, Map<String, Serializable> map) {

		if (sourceStructure instanceof MapComplexValueType) {

			for (Entry<String, GenericValueType> e : ((MapComplexValueType) sourceStructure)
					.getMap().entrySet()) {

				if (e.getValue() instanceof SimpleValueType) {
					map.put(newName, ((MapObject) serializable).getMap()
							.remove(e.getKey()));

				} else {
					if (e.getValue() instanceof MapComplexValueType)
					{
						MapComplexValueType mcvt = (MapComplexValueType) e.getValue();
						if (mcvt.getMap() != null)
						{
							traverseSchema(e.getValue(), newName, ((MapObject) serializable)
									.getMap().get(e.getKey()), map);
						}
						else
						{
							map.put(newName, ((MapObject) serializable).getMap()
									.remove(e.getKey()));
						}
					}					
				}
			}
		} else if (sourceStructure instanceof ListComplexValueType) {
			for (Serializable gv : ((ListObject) serializable).getList()) {
				traverseSchema(((ListComplexValueType) sourceStructure)
						.getList().get(0), newName, gv, map);
			}
		}
	}

	private void insertRenamedValue(Serializable serializable, MapObject mv,
			GenericValueType destinationStructure) {

		if (destinationStructure instanceof MapComplexValueType) {

			for (Entry<String, GenericValueType> e : ((MapComplexValueType) destinationStructure)
					.getMap().entrySet()) {

				if (e.getValue() == null) {

					if (serializable instanceof MapObject) {			
						Serializable ser = mv.getMap().get(e.getKey());
						
						// check for correct value here
						((MapObject) serializable).getMap().put(e.getKey(), ser);
					} else {
						String errmsg = "Invalid renamedSchema.";
						Logging.error(this.getClass(), errmsg);
						System.err.println(errmsg);
						throw new RuntimeException(errmsg);
					}

				} else {

					if (((MapObject) serializable).getMap().get(e.getKey()) == null) {
						((MapObject) serializable).getMap().put(e.getKey(),
								new MapObject());
					}

					insertRenamedValue(((MapObject) serializable).getMap()
							.get(e.getKey()), mv, e.getValue());
				}
			}
		} else if (destinationStructure instanceof ListComplexValueType) {

			if (!(serializable instanceof ListObject)) {
				String errmsg = "Invalid renameSchema.";
				Logging.error(this.getClass(), errmsg);
				System.err.println(errmsg);
				throw new RuntimeException(errmsg);
			}

			for (Serializable gv : ((ListObject) serializable).getList()) {

				insertRenamedValue(gv, mv,
						((ListComplexValueType) destinationStructure).getList()
								.get(0));
			}

		}

	}


	private static MapComplexValueType createSourceWaterStructure() {

		Map<String, GenericValueType> station = new HashMap<String, GenericValueType>();

		station.put("water", new MapComplexValueType(null));
		MapComplexValueType stationSchema = new MapComplexValueType(station);

		return stationSchema;
	}


	private static MapComplexValueType createDestinationWaterStructure() {

		Map<String, GenericValueType> station = new HashMap<String, GenericValueType>();
		station.put("BodyOfWater", null);
		MapComplexValueType stationSchema = new MapComplexValueType(
				station);

		return stationSchema;
	}

}
