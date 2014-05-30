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
import org.jvalue.ods.data.schema.AllowedBaseObjectTypes;
import org.jvalue.ods.data.schema.ListObjectType;
import org.jvalue.ods.data.schema.MapObjectType;
import org.jvalue.ods.data.schema.GenericObjectType;

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
		GenericObjectType s = createSchema();
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
	private GenericObjectType createSchema() {
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

		Map<String, GenericObjectType> station = new HashMap<String, GenericObjectType>();
		station.put("uuid", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("number", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("shortname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("longname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("km", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		station.put("agency", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("longitude", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		station.put("latitude", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		station.put("water", waterSchema);
		station.put("timeseries", timeSeriesListSchema);
		MapObjectType stationSchema = new MapObjectType(station);

		List<GenericObjectType> stationList = new LinkedList<GenericObjectType>();
		stationList.add(stationSchema);
		ListObjectType listObjectType = new ListObjectType(stationList);

		return listObjectType;
	}

}
