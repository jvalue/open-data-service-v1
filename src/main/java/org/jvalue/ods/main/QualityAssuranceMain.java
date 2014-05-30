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
package org.jvalue.ods.main;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jvalue.EnumType;
import org.jvalue.ExactValueRestriction;
import org.jvalue.ValueType;
import org.jvalue.numbers.Range;
import org.jvalue.numbers.RangeBound;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.data.schema.AllowedBaseObjectTypes;
import org.jvalue.ods.data.schema.ListObjectType;
import org.jvalue.ods.data.schema.MapObjectType;
import org.jvalue.ods.data.schema.GenericObjectType;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.filter.CombineFilter;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.qa.PegelOnlineQualityAssurance;
import org.jvalue.ods.schema.SchemaManager;
import org.jvalue.ods.translator.JsonTranslator;
import org.jvalue.si.QuantityUnitType;
import org.jvalue.si.SiUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class QualityAssuranceMain.
 */
public class QualityAssuranceMain {

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
	public static void main(String[] args) throws JsonProcessingException {

		valueTypes = new HashMap<>();
		valueTypes.put("waterLevelTrend", createWaterLevelTrendType());
		valueTypes.put("waterLevel", createWaterLevelType());
		valueTypes.put("temperature", createTemperatureType());
		valueTypes.put("electricalConductivity",
				createElectricalConductivityType());

		MapObjectType sourceCoordinateStructure = createSourceCoordinateStructure();
		MapObjectType destinationCoordinateStructure = createDestinationCoordinateStructure();
		MapObjectType combinedSchema = createCombinedSchema();

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

					GenericEntity gv = new JsonTranslator().convertJson(station);

					mv = new CombineFilter((MapObject) gv,
							sourceCoordinateStructure,
							destinationCoordinateStructure).filter();

					if (!SchemaManager.validateGenericValusFitsSchema(mv,
							combinedSchema)) {
						System.err
								.println("Validation of quality-enhanced data failed.");
					}

					try {

						qaAccessor.insert(mv);

					} catch (Exception ex) {
						String errmsg = "Could not insert MapValue: "
								+ ex.getMessage();
						System.err.println(errmsg);
						Logging.error(QualityAssuranceMain.class, errmsg);
						throw new RuntimeException(errmsg);
					}

				}
			}
			qaAccessor.insert(combinedSchema);

		} catch (DbException e) {

			String errmsg = "Error during Quality Assurance. " + e.getMessage();
			System.err.println(errmsg);
			Logging.error(QualityAssuranceMain.class, errmsg);
			throw new RuntimeException(errmsg);

		}
		new PegelOnlineQualityAssurance().checkValueTypes();

	}

	/**
	 * Creates the combined schema.
	 * 
	 * @return the map schema
	 */
	private static MapObjectType createCombinedSchema() {
		Map<String, GenericObjectType> water = new HashMap<String, GenericObjectType>();
		water.put("shortname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		water.put("longname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		MapObjectType waterSchema = new MapObjectType(water);

		Map<String, GenericObjectType> currentMeasurement = new HashMap<String, GenericObjectType>();
		currentMeasurement.put("timestamp", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		currentMeasurement.put("value", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		currentMeasurement.put("trend", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		currentMeasurement.put("stateMnwMhw", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		currentMeasurement.put("stateNswHsw", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		MapObjectType currentMeasurementSchema = new MapObjectType(currentMeasurement);

		Map<String, GenericObjectType> gaugeZero = new HashMap<String, GenericObjectType>();
		gaugeZero.put("unit", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		gaugeZero.put("value", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		gaugeZero.put("validFrom", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		MapObjectType gaugeZeroSchema = new MapObjectType(gaugeZero);

		Map<String, GenericObjectType> comment = new HashMap<String, GenericObjectType>();
		comment.put("shortDescription", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		comment.put("longDescription", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		MapObjectType commentSchema = new MapObjectType(comment);

		Map<String, GenericObjectType> timeSeries = new HashMap<String, GenericObjectType>();
		timeSeries.put("shortname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		timeSeries.put("longname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		timeSeries.put("unit", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		timeSeries.put("equidistance", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		timeSeries.put("currentMeasurement", currentMeasurementSchema);
		timeSeries.put("gaugeZero", gaugeZeroSchema);
		timeSeries.put("comment", commentSchema);
		MapObjectType timeSeriesSchema = new MapObjectType(timeSeries);

		List<GenericObjectType> timeSeriesList = new LinkedList<GenericObjectType>();
		timeSeriesList.add(timeSeriesSchema);
		ListObjectType timeSeriesListSchema = new ListObjectType(timeSeriesList);

		Map<String, GenericObjectType> coordinate = new HashMap<>();
		coordinate.put("longitude", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		coordinate.put("latitude", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		MapObjectType coordinateSchema = new MapObjectType(coordinate);

		Map<String, GenericObjectType> station = new HashMap<String, GenericObjectType>();
		station.put("coordinate", coordinateSchema);
		station.put("uuid", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("number", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("shortname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("longname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("km", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		station.put("agency", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("water", waterSchema);
		station.put("timeseries", timeSeriesListSchema);
		// two class object strings, must not be "required"
		Map<String, GenericObjectType> type = new HashMap<String, GenericObjectType>();
		type.put("Station", AllowedBaseObjectTypes.getBaseObjectType("Null"));
		MapObjectType typeSchema = new MapObjectType(type);
		station.put("objectType", typeSchema);
		Map<String, GenericObjectType> restName = new HashMap<String, GenericObjectType>();
		restName.put("stations", AllowedBaseObjectTypes.getBaseObjectType("Null"));
		MapObjectType restNameSchema = new MapObjectType(restName);
		station.put("rest_name", restNameSchema);
		MapObjectType stationSchema = new MapObjectType(station);

		return stationSchema;
	}

	/**
	 * Creates the coordinate schema.
	 * 
	 * @return the map schema
	 */
	private static MapObjectType createSourceCoordinateStructure() {

		Map<String, GenericObjectType> station = new HashMap<String, GenericObjectType>();

		station.put("longitude", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		station.put("latitude", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		MapObjectType stationSchema = new MapObjectType(station);

		return stationSchema;
	}

	/**
	 * Creates the destination coordinate structure.
	 * 
	 * @return the map schema
	 */
	private static MapObjectType createDestinationCoordinateStructure() {

		Map<String, GenericObjectType> coordinate = new HashMap<String, GenericObjectType>();

		coordinate.put("coordinate", null);
		MapObjectType coordinateSchema = new MapObjectType(coordinate);

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
