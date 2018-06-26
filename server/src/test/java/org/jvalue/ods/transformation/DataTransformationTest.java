package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;
import mockit.integration.junit4.JMockit;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.script.ScriptException;
import java.io.IOException;

@RunWith(JMockit.class)
public final class DataTransformationTest
{

	@Rule
	public ExpectedException thrown= ExpectedException.none();


	private static JsonNode data;
	private static JsonNode dataExpected;

	private static TransformationFunction transformationFunction;

	private static ExecutionEngine executionEngine;
	private static DataTransformationManager dataTransformationManager;

	private static final String simpleData = "{\"id\": \"20934\", \"main\": { \"temp\": \"22.0\"}}";
	private static final String simpleDataExpected = "{\"id\":\"20934\", \"main\": { \"temp\": \"22.0\", \"lat\":20.2332,\"lon\":10.4244}}";

	@BeforeClass
	public static void initialize() throws IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		data = mapper.readTree(simpleData);
		dataExpected = mapper.readTree(simpleDataExpected);
		executionEngine = new NashornExecutionEngine();
		dataTransformationManager = new DataTransformationManager(executionEngine);
	}

	private static final String simpleExtension =
			"function transform(doc){"
			+ "    if(doc.id != null && doc.main != null){"
			+ "        doc.main.lat = 20.2332;"
			+ "        doc.main.lon = 10.4244;"
			+ "    }"
			+ "};";

	@Test
	public void testValidTransformationExecution() throws ScriptException, IOException
	{
		transformationFunction = new TransformationFunction("1", simpleExtension);
		JsonNode result = dataTransformationManager.transform(data, transformationFunction);
		Assert.assertEquals(dataExpected, result);
	}

	@Test
	public void testInvalidTransformationExecution()
	throws ScriptException, IOException
	{
		thrown.expect(ScriptException.class);

		transformationFunction = new TransformationFunction("2", "invalid Javascript Code");
		dataTransformationManager.transform(data, transformationFunction);
	}

	private static final String infiniteLoop =
			"function transform(doc){"
			+"    while(true) { ; }"
			+"};";

	@Test
	public void testInfiniteLoopTransformationExecution()
	throws ScriptException, IOException
	{
		thrown.expect(ScriptCPUAbuseException.class);

		transformationFunction = new TransformationFunction("3", infiniteLoop);
		dataTransformationManager.transform(data, transformationFunction);
	}
}