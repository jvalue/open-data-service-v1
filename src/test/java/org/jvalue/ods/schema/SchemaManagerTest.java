/*
 * 
 */
package org.jvalue.ods.schema;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.schema.AllowedValueTypes;
import org.jvalue.ods.data.schema.GenericValueType;
import org.jvalue.ods.data.schema.ListComplexValueType;
import org.jvalue.ods.data.schema.MapComplexValueType;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.processors.syntax.SyntaxValidator;

/**
 * The Class SchemaManagerTest.
 */
public class SchemaManagerTest {

	/** The schema manager. */
	SchemaManager schemaManager;

	/**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		schemaManager = new SchemaManager();
		assertNotNull(schemaManager);
	}

	/**
	 * Test create json schema.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testCreateJsonSchema() throws IOException {
		GenericValueType s = createSchema();
		String result = SchemaManager.createJsonSchema(s);
		assertNotNull(result);

		// validate schema
		JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		JsonNode jn = JsonLoader.fromString(result);
		SyntaxValidator validator = factory.getSyntaxValidator();
		assertTrue(validator.schemaIsValid(jn));

		// PrintWriter out;
		// try {
		// out = new PrintWriter("schema.json");
		// out.print(result);
		// out.close();
		//
		//
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * Creates the schema.
	 * 
	 * @return the schema
	 */
	private GenericValueType createSchema() {
		Map<String, GenericValueType> water = new HashMap<String, GenericValueType>();
		water.put("shortname", AllowedValueTypes.VALUETYPE_STRING);
		water.put("longname", AllowedValueTypes.VALUETYPE_STRING);
		MapComplexValueType waterSchema = new MapComplexValueType(water);

		Map<String, GenericValueType> currentMeasurement = new HashMap<String, GenericValueType>();
		currentMeasurement.put("timestamp", AllowedValueTypes.VALUETYPE_STRING);
		currentMeasurement.put("value", AllowedValueTypes.VALUETYPE_NUMBER);
		currentMeasurement.put("trend", AllowedValueTypes.VALUETYPE_NUMBER);
		currentMeasurement.put("stateMnwMhw", AllowedValueTypes.VALUETYPE_STRING);
		currentMeasurement.put("stateNswHsw", AllowedValueTypes.VALUETYPE_STRING);
		MapComplexValueType currentMeasurementSchema = new MapComplexValueType(currentMeasurement);

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
		MapComplexValueType timeSeriesSchema = new MapComplexValueType(timeSeries);

		List<GenericValueType> timeSeriesList = new LinkedList<GenericValueType>();
		timeSeriesList.add(timeSeriesSchema);
		ListComplexValueType timeSeriesListSchema = new ListComplexValueType(timeSeriesList);

		Map<String, GenericValueType> station = new HashMap<String, GenericValueType>();
		station.put("uuid", AllowedValueTypes.VALUETYPE_STRING);
		station.put("number", AllowedValueTypes.VALUETYPE_STRING);
		station.put("shortname", AllowedValueTypes.VALUETYPE_STRING);
		station.put("longname", AllowedValueTypes.VALUETYPE_STRING);
		station.put("km", AllowedValueTypes.VALUETYPE_NUMBER);
		station.put("agency", AllowedValueTypes.VALUETYPE_STRING);
		station.put("longitude", AllowedValueTypes.VALUETYPE_NUMBER);
		station.put("latitude", AllowedValueTypes.VALUETYPE_NUMBER);
		station.put("water", waterSchema);
		station.put("timeseries", timeSeriesListSchema);
		MapComplexValueType stationSchema = new MapComplexValueType(station);

		List<GenericValueType> stationList = new LinkedList<GenericValueType>();
		stationList.add(stationSchema);
		ListComplexValueType listObjectType = new ListComplexValueType(stationList);

		return listObjectType;
	}

}
