package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(JMockit.class)
public final class DataTransformationTest
{
	private DataTransformation dataTransformation;

	private static JsonNode data;
	private static JsonNode dataExpected;

	private static TransformationFunction transformationFunction;

	//Todo move into file
	private static final String simpleExtension =
			"  var transform = function(doc){"
			+ "    if(doc.id != null && doc.main != null){"
			+ "        doc.main.lat = 20.2332;"
			+ "        doc.main.lon = 10.4244;"
			+ "    }"
			+ "};";

	private static final String simpleData = "{\"id\": \"20934\", \"main\": { \"temp\": \"22.0\"}}";
	private static final String simpleDataExpected = "{\"id\":\"20934\", \"main\": { \"temp\": \"22.0\", \"lat\":20.2332,\"lon\":10.4244}}";

	@BeforeClass
	public static void initialize() throws IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		data = mapper.readTree(simpleData);

		dataExpected = mapper.readTree(simpleDataExpected);
		transformationFunction = new TransformationFunction("1", simpleExtension);
	}


	@Test
	public void testTransformationExecution() throws IOException
	{
		ExecutionEngine executionEngine = new NashornExecutionEngine();
		TransformationManager manager = new TransformationManager(executionEngine);

		dataTransformation = new DataTransformation(data, transformationFunction);

		JsonNode result = manager.executeTransformation(dataTransformation);

		Assert.assertEquals(dataExpected, result);
	}

}