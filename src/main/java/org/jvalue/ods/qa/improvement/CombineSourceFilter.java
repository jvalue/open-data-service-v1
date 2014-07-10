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

import org.jvalue.EnumType;
import org.jvalue.ExactValueRestriction;
import org.jvalue.ValueType;
import org.jvalue.numbers.Range;
import org.jvalue.numbers.RangeBound;
import org.jvalue.ods.data.generic.BaseObject;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.data.valuetypes.AllowedValueTypes;
import org.jvalue.ods.data.valuetypes.GenericValueType;
import org.jvalue.ods.data.valuetypes.ListComplexValueType;
import org.jvalue.ods.data.valuetypes.MapComplexValueType;
import org.jvalue.ods.data.valuetypes.SimpleValueType;
import org.jvalue.ods.filter.Filter;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.qa.PegelOnlineQualityAssurance;
import org.jvalue.si.QuantityUnitType;
import org.jvalue.si.SiUnit;


public final class CombineSourceFilter implements Filter<GenericEntity, GenericEntity> {

	@Override
	public GenericEntity filter(GenericEntity data) {
		// if (!SchemaManager.validateGenericValusFitsSchema(data, schema)) {
		// Logging.info(this.getClass(),
		// "Could not validate schema in CombineFilter.");
		// return data;
		// }
		// TODO

		Map<String, ValueType<?>> valueTypes = new HashMap<>();
		valueTypes.put("waterLevelTrend", createWaterLevelTrendType());
		valueTypes.put("waterLevel", createWaterLevelType());
		valueTypes.put("temperature", createTemperatureType());
		valueTypes.put("electricalConductivity",
				createElectricalConductivityType());

		MapComplexValueType sourceCoordinateStructure = createSourceCoordinateStructure();
		MapComplexValueType destinationCoordinateStructure = createDestinationCoordinateStructure();

		MapObject mv = new MapObject();

		List<Serializable> improvedObjects = new LinkedList<Serializable>();
		ListObject oldObjects = (ListObject) data;

		for (Serializable s : oldObjects.getList()) {
			GenericEntity gv = (GenericEntity) s;

			traverseSchema(sourceCoordinateStructure, gv, mv.getMap());

			insertCombinedValue(gv, mv, destinationCoordinateStructure);

			MapObject finalMo = (MapObject) gv;
			if (!finalMo.getMap().containsKey("dataStatus")) {
				finalMo.getMap().put("dataStatus",
					new BaseObject("improved"));
			}

			improvedObjects.add(gv);
		}

		new PegelOnlineQualityAssurance().checkValueTypes(valueTypes);

		return new ListObject(improvedObjects);
	}


	private void traverseSchema(GenericValueType sourceStructure,
			Serializable serializable, Map<String, Serializable> map) {

		if (sourceStructure instanceof MapComplexValueType) {

			for (Entry<String, GenericValueType> e : ((MapComplexValueType) sourceStructure)
					.getMap().entrySet()) {

				if ((e.getValue() instanceof SimpleValueType)
						&& (((SimpleValueType) e.getValue()).getName() != "Null")) {

					map.put(e.getKey(), ((MapObject) serializable).getMap()
							.remove(e.getKey()));

				} else {
					traverseSchema(e.getValue(), ((MapObject) serializable)
							.getMap().get(e.getKey()), map);
				}
			}
		} else if (sourceStructure instanceof ListComplexValueType) {

			for (Serializable gv : ((ListObject) serializable).getList()) {

				traverseSchema(((ListComplexValueType) sourceStructure)
						.getList().get(0), gv, map);

			}

		}
	}

	/**
	 * Insert combined value.
	 * 
	 * @param serializable
	 *            the serializable
	 * @param mv
	 *            the mv
	 * @param destinationStructure
	 *            the destination structure
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

					insertCombinedValue(((MapObject) serializable).getMap()
							.get(e.getKey()), mv, e.getValue());
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

				insertCombinedValue(gv, mv,
						((ListComplexValueType) destinationStructure).getList()
								.get(0));
			}

		}

	}


	/**
	 * Creates the coordinate schema.
	 * 
	 * @return the map schema
	 */
	private static MapComplexValueType createSourceCoordinateStructure() {

		Map<String, GenericValueType> station = new HashMap<String, GenericValueType>();

		station.put("longitude", AllowedValueTypes.VALUETYPE_NUMBER);
		station.put("latitude", AllowedValueTypes.VALUETYPE_NUMBER);
		MapComplexValueType stationSchema = new MapComplexValueType(station);

		return stationSchema;
	}

	/**
	 * Creates the destination coordinate structure.
	 * 
	 * @return the map schema
	 */
	private static MapComplexValueType createDestinationCoordinateStructure() {

		Map<String, GenericValueType> coordinate = new HashMap<String, GenericValueType>();

		coordinate.put("coordinate", null);
		MapComplexValueType coordinateSchema = new MapComplexValueType(
				coordinate);

		return coordinateSchema;
	}

	/**
	 * Creates the water level trend type.
	 * 
	 * @return the enum type
	 */
	private static EnumType createWaterLevelTrendType() {
		ExactValueRestriction<String> a = new ExactValueRestriction<String>(
				"-1");
		ExactValueRestriction<String> b = new ExactValueRestriction<String>("0");
		ExactValueRestriction<String> c = new ExactValueRestriction<String>("1");
		ExactValueRestriction<String> d = new ExactValueRestriction<String>(
				"-999");

		EnumType trendType = new EnumType(a.or(b).or(c).or(d));
		return trendType;
	}

	/**
	 * Creates the water level type.
	 * 
	 * @return the quantity unit type
	 */
	private static QuantityUnitType createWaterLevelType() {

		RangeBound<Double> low = new RangeBound<Double>(0.0);
		RangeBound<Double> high = new RangeBound<Double>(Double.MAX_VALUE);
		Range<Double> range = new Range<Double>(low, high);
		return new QuantityUnitType(range, SiUnit.m);
	}

	/**
	 * Creates the temperature type.
	 * 
	 * @return the quantity unit type
	 */
	private static QuantityUnitType createTemperatureType() {

		RangeBound<Double> low = new RangeBound<Double>(0.0);
		RangeBound<Double> high = new RangeBound<Double>(Double.MAX_VALUE);
		Range<Double> range = new Range<Double>(low, high);
		return new QuantityUnitType(range, SiUnit.K);
	}

	/**
	 * Creates the electrical conductivity type.
	 * 
	 * @return the quantity unit type
	 */
	private static QuantityUnitType createElectricalConductivityType() {

		RangeBound<Double> low = new RangeBound<Double>(0.0);
		RangeBound<Double> high = new RangeBound<Double>(Double.MAX_VALUE);
		Range<Double> range = new Range<Double>(low, high);
		return new QuantityUnitType(range, SiUnit.s.divide(SiUnit.m));
	}

}
