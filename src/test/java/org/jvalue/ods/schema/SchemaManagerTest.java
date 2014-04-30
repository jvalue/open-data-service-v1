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
import org.jvalue.ods.data.schema.ListSchema;
import org.jvalue.ods.data.schema.MapSchema;
import org.jvalue.ods.data.schema.NumberSchema;
import org.jvalue.ods.data.schema.Schema;
import org.jvalue.ods.data.schema.StringSchema;
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
		Schema s = createSchema();
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
	private Schema createSchema() {
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

		Map<String, Schema> station = new HashMap<String, Schema>();
		station.put("uuid", new StringSchema());
		station.put("number", new StringSchema());
		station.put("shortname", new StringSchema());
		station.put("longname", new StringSchema());
		station.put("km", new NumberSchema());
		station.put("agency", new StringSchema());
		station.put("longitude", new NumberSchema());
		station.put("latitude", new NumberSchema());
		station.put("water", waterSchema);
		station.put("timeseries", timeSeriesListSchema);
		MapSchema stationSchema = new MapSchema(station);

		List<Schema> stationList = new LinkedList<Schema>();
		stationList.add(stationSchema);
		ListSchema listSchema = new ListSchema(stationList);

		return listSchema;
	}

}
