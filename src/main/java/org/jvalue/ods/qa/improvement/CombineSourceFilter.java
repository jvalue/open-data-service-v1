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

public final class CombineSourceFilter implements Filter<Object, Object> {

	@Override
	@SuppressWarnings("unchecked")
	public Object filter(Object data) {

		if (data == null) {
			throw new IllegalArgumentException();
		}

		Map<String, ValueType<?>> valueTypes = new HashMap<>();
		valueTypes.put("waterLevelTrend", createWaterLevelTrendType());
		valueTypes.put("waterLevel", createWaterLevelType());
		valueTypes.put("temperature", createTemperatureType());
		valueTypes.put("electricalConductivity",
				createElectricalConductivityType());

		MapComplexValueType sourceCoordinateStructure = createSourceCoordinateStructure();
		MapComplexValueType destinationCoordinateStructure = createDestinationCoordinateStructure();

		List<Object> improvedObjects = new LinkedList<Object>();
		List<Object> oldObjects = (List<Object>) data;

		for (Object gv : oldObjects) {

			Map<String, Object> map = new HashMap<String, Object>();
			traverseSchema(sourceCoordinateStructure, gv, map);

			insertCombinedValue(gv, map, destinationCoordinateStructure);

			Map<String, Object> finalMo = (Map<String, Object>) gv;
			finalMo.put("dataQualityStatus", "improved");

			try {
				((Map<String, Object>) gv).remove("_id");
				((Map<String, Object>) gv).remove("_rev");
			} catch (Exception e) {
				Logging.error(this.getClass(), e.getMessage());
			}
			improvedObjects.add(gv);
		}

		new PegelOnlineQualityAssurance().checkValueTypes(valueTypes);

		return improvedObjects;
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
						Logging.error(this.getClass(), errmsg);
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
				Logging.error(this.getClass(), errmsg);
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

	private static MapComplexValueType createSourceCoordinateStructure() {

		Map<String, GenericValueType> station = new HashMap<String, GenericValueType>();

		station.put("longitude", AllowedValueTypes.VALUETYPE_NUMBER);
		station.put("latitude", AllowedValueTypes.VALUETYPE_NUMBER);
		MapComplexValueType stationSchema = new MapComplexValueType(station);

		return stationSchema;
	}

	private static MapComplexValueType createDestinationCoordinateStructure() {

		Map<String, GenericValueType> coordinate = new HashMap<String, GenericValueType>();

		coordinate.put("coordinate", null);
		MapComplexValueType coordinateSchema = new MapComplexValueType(
				coordinate);

		return coordinateSchema;
	}

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

	private static QuantityUnitType createWaterLevelType() {

		RangeBound<Double> low = new RangeBound<Double>(0.0);
		RangeBound<Double> high = new RangeBound<Double>(Double.MAX_VALUE);
		Range<Double> range = new Range<Double>(low, high);
		return new QuantityUnitType(range, SiUnit.m);
	}

	private static QuantityUnitType createTemperatureType() {

		RangeBound<Double> low = new RangeBound<Double>(0.0);
		RangeBound<Double> high = new RangeBound<Double>(Double.MAX_VALUE);
		Range<Double> range = new Range<Double>(low, high);
		return new QuantityUnitType(range, SiUnit.K);
	}

	private static QuantityUnitType createElectricalConductivityType() {

		RangeBound<Double> low = new RangeBound<Double>(0.0);
		RangeBound<Double> high = new RangeBound<Double>(Double.MAX_VALUE);
		Range<Double> range = new Range<Double>(low, high);
		return new QuantityUnitType(range, SiUnit.s.divide(SiUnit.m));
	}

}
