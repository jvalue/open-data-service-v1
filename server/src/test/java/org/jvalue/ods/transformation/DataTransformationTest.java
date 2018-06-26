package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;
import mockit.integration.junit4.JMockit;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

@RunWith(JMockit.class)
public final class DataTransformationTest
{

	private static JsonNode data;
	private static ExecutionEngine executionEngine;
	private static DataTransformationManager dataTransformationManager;
	private static TransformationFunction transformationFunction;

	private static String sampleData;

	@BeforeClass
	public static void initialize() throws IOException, URISyntaxException
	{
		ObjectMapper mapper = new ObjectMapper();
		URL resource = DataTransformationTest.class.getClassLoader().getResource("js/SampleWeatherData");

		File sampleWeatherData = new File(resource.toURI());
		sampleData = FileUtils.readFileToString(sampleWeatherData);

		executionEngine = new NashornExecutionEngine();
		dataTransformationManager = new DataTransformationManager(executionEngine);
		data = mapper.readTree(sampleData);
	}

	private static final String simpleExtension =
			"function transform(doc){"
			+ "    if(doc.main != null){"
			+ "        doc.main.extension = \"This is an extension\";"
			+ "    }"
			+ "    return doc;"
			+ "};";

	@Test
	public void testExtensionTransformationExecution() throws ScriptException, IOException
	{
		transformationFunction = new TransformationFunction("1", simpleExtension);
		JsonNode result = dataTransformationManager.transform(data, transformationFunction);

		Assert.assertEquals("This is an extension", result.get("main").get("extension").asText());
	}

	private static final String simpleReduction =
"		function transform(doc){"
		+ "if(doc != null){"
		+"		return Object.keys(doc).reduce("
		+ "			function(previous, key) {"
		+ "				previous.keycount ++;"
		+ "				return previous;"
		+ "			}, {keycount: 0})"
		+"		}"
		+"}";

	@Test
	public void testReduceTransformationExecution() throws ScriptException, IOException
	{
		transformationFunction = new TransformationFunction("1", simpleReduction);
		JsonNode result = dataTransformationManager.transform(data, transformationFunction);

		Assert.assertEquals(12, result.get("keycount").intValue());
	}


	private static final String simpleMap =
			"		function transform(doc){"
					+ "if(doc != null){"
					+"		Object.keys(doc).map("
					+ "			function(key, index) {"
					+ "				if(key === 'coord' || key === 'main'){ "
					+ "					doc[key].newEntry = \"New Entry\";"
					+ "				}"
					+ "			});"
					+"	}"
					+ " return doc;"
					+"}";


	@Test
	public void testMapTransformationExecution() throws ScriptException, IOException
	{
		transformationFunction = new TransformationFunction("1", simpleMap);
		JsonNode result = dataTransformationManager.transform(data, transformationFunction);

		Assert.assertEquals("New Entry", result.get("coord").get("newEntry").asText());
		Assert.assertEquals("New Entry", result.get("main").get("newEntry").asText());
	}

	@Test(expected = ScriptException.class)
	public void testInvalidTransformationExecution()
	throws ScriptException, IOException
	{
		transformationFunction = new TransformationFunction("2", "invalid Javascript Code");
		dataTransformationManager.transform(data, transformationFunction);
	}

	private static final String infiniteLoop =
			"function transform(doc){"
			+"    while(true) { ; }"
			+"};";

	@Test(expected = ScriptCPUAbuseException.class)
	public void testInfiniteLoopTransformationExecution()
	throws ScriptException, IOException
	{
		transformationFunction = new TransformationFunction("3", infiniteLoop);
		dataTransformationManager.transform(data, transformationFunction);
	}


}