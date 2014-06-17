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
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.data.valuetypes.AllowedValueTypes;
import org.jvalue.ods.data.valuetypes.GenericValueType;
import org.jvalue.ods.data.valuetypes.ListComplexValueType;
import org.jvalue.ods.data.valuetypes.MapComplexValueType;
import org.jvalue.ods.data.valuetypes.SimpleValueType;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.qa.PegelOnlineQualityAssurance;
import org.jvalue.ods.translator.JsonTranslator;
import org.jvalue.si.QuantityUnitType;
import org.jvalue.si.SiUnit;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class CombineFilter.
 */
public class CombineFilter extends OdsFilter<Void, Void> {

	/**
	 * Filter.
	 * 
	 * @return the map value
	 */
	@Override
	protected Void filterHelper(DataSource source, Void param) {

		// if (!SchemaManager.validateGenericValusFitsSchema(data, schema)) {
		// Logging.info(this.getClass(),
		// "Could not validate schema in CombineFilter.");
		// return data;
		// }
		// TODO

		valueTypes = new HashMap<>();
		valueTypes.put("waterLevelTrend", createWaterLevelTrendType());
		valueTypes.put("waterLevel", createWaterLevelType());
		valueTypes.put("temperature", createTemperatureType());
		valueTypes.put("electricalConductivity",
				createElectricalConductivityType());

		MapComplexValueType sourceCoordinateStructure = createSourceCoordinateStructure();
		MapComplexValueType destinationCoordinateStructure = createDestinationCoordinateStructure();
		MapComplexValueType combinedSchema = createCombinedSchema();

		MapObject mv = null;

		try {
			accessor = DbFactory.createDbAccessor("ods");
			accessor.connect();
			qaAccessor = DbFactory.createDbAccessor("ods_qa");
			qaAccessor.connect();
			qaAccessor.deleteDatabase();

			List<JsonNode> nodes = accessor.executeDocumentQuery(
					"_design/pegelonline", "getAllStations", null);

			for (JsonNode station : nodes) {
				if (station.isObject()) {

					GenericEntity gv = JsonTranslator.INSTANCE
							.convertJson(station);

					traverseSchema(sourceCoordinateStructure, gv,
							((MapObject) gv).getMap());

					insertCombinedValue(gv, mv, destinationCoordinateStructure);

					// if (!SchemaManager.validateGenericValusFitsSchema(mv,
					// combinedSchema)) {
					// System.err
					// .println("Validation of quality-enhanced data failed.");
					// }

					// TODO

					try {

						qaAccessor.insert(mv);

					} catch (Exception ex) {
						String errmsg = "Could not insert MapValue: "
								+ ex.getMessage();
						System.err.println(errmsg);
						Logging.error(this.getClass(), errmsg);
						throw new RuntimeException(errmsg);
					}

				}
			}
			qaAccessor.insert(combinedSchema);

		} catch (DbException e) {

			String errmsg = "Error during Quality Assurance. " + e.getMessage();
			System.err.println(errmsg);
			Logging.error(this.getClass(), errmsg);
			throw new RuntimeException(errmsg);

		}
		new PegelOnlineQualityAssurance().checkValueTypes();

		return null;
	}

	/**
	 * Traverse schema.
	 * 
	 * @param sourceStructure
	 *            the schema
	 * @param serializable
	 *            the data
	 * @param map
	 *            the map
	 */
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

	/** The value types. */
	private static Map<String, ValueType<?>> valueTypes;

	/** The accessor. */
	private static DbAccessor<JsonNode> accessor;

	/** The qa accessor. */
	private static DbAccessor<JsonNode> qaAccessor;

	/**
	 * Gets the value types.
	 * 
	 * @return the value types
	 */
	public static Map<String, ValueType<?>> getValueTypes() {
		return valueTypes;
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws JsonProcessingException
	 *             the json processing exception
	 */

	/**
	 * Creates the combined schema.
	 * 
	 * @return the map schema
	 */
	private static MapComplexValueType createCombinedSchema() {
		Map<String, GenericValueType> water = new HashMap<String, GenericValueType>();
		water.put("shortname", AllowedValueTypes.VALUETYPE_STRING);
		water.put("longname", AllowedValueTypes.VALUETYPE_STRING);
		MapComplexValueType waterSchema = new MapComplexValueType(water);

		Map<String, GenericValueType> currentMeasurement = new HashMap<String, GenericValueType>();
		currentMeasurement.put("timestamp", AllowedValueTypes.VALUETYPE_STRING);
		currentMeasurement.put("value", AllowedValueTypes.VALUETYPE_NUMBER);
		currentMeasurement.put("trend", AllowedValueTypes.VALUETYPE_NUMBER);
		currentMeasurement.put("stateMnwMhw",
				AllowedValueTypes.VALUETYPE_STRING);
		currentMeasurement.put("stateNswHsw",
				AllowedValueTypes.VALUETYPE_STRING);
		MapComplexValueType currentMeasurementSchema = new MapComplexValueType(
				currentMeasurement);

		Map<String, GenericValueType> gaugeZero = new HashMap<String, GenericValueType>();
		gaugeZero.put("unit", AllowedValueTypes.VALUETYPE_STRING);
		gaugeZero.put("value", AllowedValueTypes.VALUETYPE_NUMBER);
		gaugeZero.put("validFrom", AllowedValueTypes.VALUETYPE_STRING);
		MapComplexValueType gaugeZeroSchema = new MapComplexValueType(gaugeZero);

		Map<String, GenericValueType> comment = new HashMap<String, GenericValueType>();
		comment.put("shortDescription", AllowedValueTypes.VALUETYPE_STRING);
		comment.put("longDescription", AllowedValueTypes.VALUETYPE_STRING);
		MapComplexValueType commentSchema = new MapComplexValueType(comment);

		Map<String, GenericValueType> timeSeries = new HashMap<String, GenericValueType>();
		timeSeries.put("shortname", AllowedValueTypes.VALUETYPE_STRING);
		timeSeries.put("longname", AllowedValueTypes.VALUETYPE_STRING);
		timeSeries.put("unit", AllowedValueTypes.VALUETYPE_STRING);
		timeSeries.put("equidistance", AllowedValueTypes.VALUETYPE_NUMBER);
		timeSeries.put("currentMeasurement", currentMeasurementSchema);
		timeSeries.put("gaugeZero", gaugeZeroSchema);
		timeSeries.put("comment", commentSchema);
		MapComplexValueType timeSeriesSchema = new MapComplexValueType(
				timeSeries);

		List<GenericValueType> timeSeriesList = new LinkedList<GenericValueType>();
		timeSeriesList.add(timeSeriesSchema);
		ListComplexValueType timeSeriesListSchema = new ListComplexValueType(
				timeSeriesList);

		Map<String, GenericValueType> coordinate = new HashMap<>();
		coordinate.put("longitude", AllowedValueTypes.VALUETYPE_NUMBER);
		coordinate.put("latitude", AllowedValueTypes.VALUETYPE_NUMBER);
		MapComplexValueType coordinateSchema = new MapComplexValueType(
				coordinate);

		Map<String, GenericValueType> station = new HashMap<String, GenericValueType>();
		station.put("coordinate", coordinateSchema);
		station.put("uuid", AllowedValueTypes.VALUETYPE_STRING);
		station.put("number", AllowedValueTypes.VALUETYPE_STRING);
		station.put("shortname", AllowedValueTypes.VALUETYPE_STRING);
		station.put("longname", AllowedValueTypes.VALUETYPE_STRING);
		station.put("km", AllowedValueTypes.VALUETYPE_NUMBER);
		station.put("agency", AllowedValueTypes.VALUETYPE_STRING);
		station.put("water", waterSchema);
		station.put("timeseries", timeSeriesListSchema);
		// two class object strings, must not be "required"
		Map<String, GenericValueType> type = new HashMap<String, GenericValueType>();
		type.put("Station", AllowedValueTypes.VALUETYPE_NULL);
		MapComplexValueType typeSchema = new MapComplexValueType(type);
		station.put("objectType", typeSchema);
		Map<String, GenericValueType> restName = new HashMap<String, GenericValueType>();
		restName.put("stations", AllowedValueTypes.VALUETYPE_NULL);
		MapComplexValueType restNameSchema = new MapComplexValueType(restName);
		station.put("rest_name", restNameSchema);
		MapComplexValueType stationSchema = new MapComplexValueType(station);

		return stationSchema;
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
