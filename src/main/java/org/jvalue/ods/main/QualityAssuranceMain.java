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
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericValue;
import org.jvalue.ods.data.generic.MapValue;
import org.jvalue.ods.data.schema.ListSchema;
import org.jvalue.ods.data.schema.MapSchema;
import org.jvalue.ods.data.schema.NullSchema;
import org.jvalue.ods.data.schema.NumberSchema;
import org.jvalue.ods.data.schema.Schema;
import org.jvalue.ods.data.schema.StringSchema;
import org.jvalue.ods.filter.CombineFilter;
import org.jvalue.ods.grabber.Translator;
import org.jvalue.ods.qa.PegelOnlineQualityAssurance;
import org.jvalue.ods.schema.SchemaManager;
import org.jvalue.ods.translator.JsonTranslator;
import org.jvalue.si.QuantityUnitType;
import org.jvalue.si.SiUnit;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * The Class QualityAssuranceMain.
 */
public class QualityAssuranceMain {

	private static Map <String, ValueType<?>> valueTypes;
	
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
		valueTypes.put("electricalConductivity", createElectricalConductivityType());
		
		Translator jt = new JsonTranslator();

		GenericValue gv = jt
				.translate(new DataSource(
						"http://faui2o2f.cs.fau.de:8080/open-data-service/ods/de/pegelonline/stations/rethem",
						null));

		MapSchema sourceCoordinateStructure = createSourceCoordinateStructure();
		MapSchema destinationCoordinateStructure = createDestinationCoordinateStructure();
		MapSchema combinedSchema = createCombinedSchema();

		MapValue mv = new CombineFilter((MapValue) gv,
				sourceCoordinateStructure, destinationCoordinateStructure)
				.filter();

		if (!SchemaManager.validateGenericValusFitsSchema(mv, combinedSchema)) {
			System.err.println("Validation of quality-enhanced data failed.");
		}

		//System.out.println(new ObjectMapper().writeValueAsString(mv));

		new PegelOnlineQualityAssurance().checkValueTypes();
		
	}

	private static MapSchema createCombinedSchema() {
		Map<String, Schema> water = new HashMap<String, Schema>();
		water.put("shortname", new StringSchema());
		water.put("longname", new StringSchema());
		MapSchema waterSchema = new MapSchema(water);

		Map<String, Schema> currentMeasurement = new HashMap<String, Schema>();
		currentMeasurement.put("timestamp", new StringSchema());
		currentMeasurement.put("value", new NumberSchema());
		currentMeasurement.put("trend", new NumberSchema());
		currentMeasurement.put("stateMnwMhw", new StringSchema());
		currentMeasurement.put("stateNswHsw", new StringSchema());
		MapSchema currentMeasurementSchema = new MapSchema(currentMeasurement);

		Map<String, Schema> gaugeZero = new HashMap<String, Schema>();
		gaugeZero.put("unit", new StringSchema());
		gaugeZero.put("value", new NumberSchema());
		gaugeZero.put("validFrom", new StringSchema());
		MapSchema gaugeZeroSchema = new MapSchema(gaugeZero);

		Map<String, Schema> comment = new HashMap<String, Schema>();
		comment.put("shortDescription", new StringSchema());
		comment.put("longDescription", new StringSchema());
		MapSchema commentSchema = new MapSchema(comment);

		Map<String, Schema> timeSeries = new HashMap<String, Schema>();
		timeSeries.put("shortname", new StringSchema());
		timeSeries.put("longname", new StringSchema());
		timeSeries.put("unit", new StringSchema());
		timeSeries.put("equidistance", new NumberSchema());
		timeSeries.put("currentMeasurement", currentMeasurementSchema);
		timeSeries.put("gaugeZero", gaugeZeroSchema);
		timeSeries.put("comment", commentSchema);
		MapSchema timeSeriesSchema = new MapSchema(timeSeries);

		List<Schema> timeSeriesList = new LinkedList<Schema>();
		timeSeriesList.add(timeSeriesSchema);
		ListSchema timeSeriesListSchema = new ListSchema(timeSeriesList);

		Map<String, Schema> coordinate = new HashMap<>();
		coordinate.put("longitude", new NumberSchema());
		coordinate.put("latitude", new NumberSchema());
		MapSchema coordinateSchema = new MapSchema(coordinate);

		Map<String, Schema> station = new HashMap<String, Schema>();
		station.put("coordinate", coordinateSchema);
		station.put("uuid", new StringSchema());
		station.put("number", new StringSchema());
		station.put("shortname", new StringSchema());
		station.put("longname", new StringSchema());
		station.put("km", new NumberSchema());
		station.put("agency", new StringSchema());
		station.put("water", waterSchema);
		station.put("timeseries", timeSeriesListSchema);
		// two class object strings, must not be "required"
		Map<String, Schema> type = new HashMap<String, Schema>();
		type.put("Station", new NullSchema());
		MapSchema typeSchema = new MapSchema(type);
		station.put("objectType", typeSchema);
		Map<String, Schema> restName = new HashMap<String, Schema>();
		restName.put("stations", new NullSchema());
		MapSchema restNameSchema = new MapSchema(restName);
		station.put("rest_name", restNameSchema);
		MapSchema stationSchema = new MapSchema(station);

		return stationSchema;
	}

	/**
	 * Creates the coordinate schema.
	 * 
	 * @return the map schema
	 */
	private static MapSchema createSourceCoordinateStructure() {

		Map<String, Schema> station = new HashMap<String, Schema>();

		station.put("longitude", new NumberSchema());
		station.put("latitude", new NumberSchema());
		MapSchema stationSchema = new MapSchema(station);

		return stationSchema;
	}

	private static MapSchema createDestinationCoordinateStructure() {

		Map<String, Schema> coordinate = new HashMap<String, Schema>();

		coordinate.put("coordinate", null);
		MapSchema coordinateSchema = new MapSchema(coordinate);

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
