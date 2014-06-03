/*
 * 
 */
package org.jvalue.ods.schema;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.schema.AllowedValueTypes;
import org.jvalue.ods.data.schema.SimpleValueType;
import org.jvalue.ods.data.schema.ListComplexValueType;
import org.jvalue.ods.data.schema.MapComplexValueType;
import org.jvalue.ods.data.schema.GenericValueType;
import org.jvalue.ods.data.schema.ObjectTypeEnum;

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
		AllowedValueTypes.addBaseObjectType("Null", new SimpleValueType("Null", ObjectTypeEnum.Domain));
		AllowedValueTypes.addBaseObjectType("java.lang.String", new SimpleValueType("java.lang.String", ObjectTypeEnum.Domain));
		AllowedValueTypes.addBaseObjectType("java.lang.Number", new SimpleValueType("java.lang.Number", ObjectTypeEnum.Domain));
		AllowedValueTypes.addBaseObjectType("java.lang.Boolean", new SimpleValueType("java.lang.Boolean", ObjectTypeEnum.Domain));
		
		
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
		water.put("shortname", AllowedValueTypes.getGenericValueType("java.lang.String"));
		water.put("longname", AllowedValueTypes.getGenericValueType("java.lang.String"));
		MapComplexValueType waterSchema = new MapComplexValueType(water);

		Map<String, GenericValueType> currentMeasurement = new HashMap<String, GenericValueType>();
		currentMeasurement.put("timestamp", AllowedValueTypes.getGenericValueType("java.lang.String"));
		currentMeasurement.put("value", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		currentMeasurement.put("trend", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		currentMeasurement.put("stateMnwMhw", AllowedValueTypes.getGenericValueType("java.lang.String"));
		currentMeasurement.put("stateNswHsw", AllowedValueTypes.getGenericValueType("java.lang.String"));
		MapComplexValueType currentMeasurementSchema = new MapComplexValueType(currentMeasurement);

		Map<String, GenericValueType> gaugeZero = new HashMap<String, GenericValueType>();
		gaugeZero.put("unit", AllowedValueTypes.getGenericValueType("java.lang.String"));
		gaugeZero.put("value", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		gaugeZero.put("validFrom", AllowedValueTypes.getGenericValueType("java.lang.String"));
		MapComplexValueType gaugeZeroSchema = new MapComplexValueType(gaugeZero);

		Map<String, GenericValueType> comment = new HashMap<String, GenericValueType>();
		comment.put("shortDescription", AllowedValueTypes.getGenericValueType("java.lang.String"));
		comment.put("longDescription", AllowedValueTypes.getGenericValueType("java.lang.String"));
		MapComplexValueType commentSchema = new MapComplexValueType(comment);

		Map<String, GenericValueType> timeSeries = new HashMap<String, GenericValueType>();
		timeSeries.put("shortname", AllowedValueTypes.getGenericValueType("java.lang.String"));
		timeSeries.put("longname", AllowedValueTypes.getGenericValueType("java.lang.String"));
		timeSeries.put("unit", AllowedValueTypes.getGenericValueType("java.lang.String"));
		timeSeries.put("equidistance", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		timeSeries.put("currentMeasurement", currentMeasurementSchema);
		timeSeries.put("gaugeZero", gaugeZeroSchema);
		timeSeries.put("comment", commentSchema);
		MapComplexValueType timeSeriesSchema = new MapComplexValueType(timeSeries);

		List<GenericValueType> timeSeriesList = new LinkedList<GenericValueType>();
		timeSeriesList.add(timeSeriesSchema);
		ListComplexValueType timeSeriesListSchema = new ListComplexValueType(timeSeriesList);

		Map<String, GenericValueType> station = new HashMap<String, GenericValueType>();
		station.put("uuid", AllowedValueTypes.getGenericValueType("java.lang.String"));
		station.put("number", AllowedValueTypes.getGenericValueType("java.lang.String"));
		station.put("shortname", AllowedValueTypes.getGenericValueType("java.lang.String"));
		station.put("longname", AllowedValueTypes.getGenericValueType("java.lang.String"));
		station.put("km", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		station.put("agency", AllowedValueTypes.getGenericValueType("java.lang.String"));
		station.put("longitude", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		station.put("latitude", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		station.put("water", waterSchema);
		station.put("timeseries", timeSeriesListSchema);
		MapComplexValueType stationSchema = new MapComplexValueType(station);

		List<GenericValueType> stationList = new LinkedList<GenericValueType>();
		stationList.add(stationSchema);
		ListComplexValueType listObjectType = new ListComplexValueType(stationList);

		return listObjectType;
	}

}
