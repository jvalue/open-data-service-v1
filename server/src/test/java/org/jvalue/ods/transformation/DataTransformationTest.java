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

	private static JsonNode jsonData;
	private static ExecutionEngine executionEngine;
	private static DataTransformationManager dataTransformationManager;
	private static TransformationFunction transformationFunction;
	private static ObjectMapper mapper;

	private static String sampleData;


	@BeforeClass
	public static void initialize() throws IOException, URISyntaxException
	{
		mapper = new ObjectMapper();
		URL resource = DataTransformationTest.class.getClassLoader().getResource("js/SampleWeatherData");

		File sampleWeatherData = new File(resource.toURI());
		sampleData = FileUtils.readFileToString(sampleWeatherData);

		executionEngine = new NashornExecutionEngine();
		dataTransformationManager = new DataTransformationManager(executionEngine);
		jsonData = mapper.readTree(sampleData);
	}

	private static final String simpleExtension =
			"function transform(dataString){"
			+ "	   var GENERIC_DATA_STRING = JSON.parse(dataString);"
			+ "    if(GENERIC_DATA_STRING.main != null){"
			+ "        GENERIC_DATA_STRING.main.extension = \"This is an extension\";"
			+ "    }"
			+ "    return JSON.stringify(GENERIC_DATA_STRING);"
			+ "};";

	@Test
	public void testExtensionTransformationExecution() throws ScriptException, IOException
	{
		transformationFunction = new TransformationFunction("1", simpleExtension);
		String result = dataTransformationManager.transform(jsonData, transformationFunction);

		JsonNode jsonNode = mapper.readTree(result);
		Assert.assertEquals("This is an extension", jsonNode.get("main").get("extension").asText());
	}

	private static final String simpleReduction =
"		function transform(dataString){"
		+ "var GENERIC_DATA_STRING = JSON.parse(dataString);"
		+ "if(GENERIC_DATA_STRING != null){"
		+"		var result = Object.keys(GENERIC_DATA_STRING).reduce("
		+ "			function(previous, key) {"
		+ "				previous.keycount ++;"
		+ "				return previous;"
		+ "			}, {keycount: 0});"
		+ "			return JSON.stringify(result);"
		+"		}"
		+"}";

	@Test
	public void testReduceTransformationExecution() throws ScriptException, IOException
	{
		transformationFunction = new TransformationFunction("1", simpleReduction);
		String result = dataTransformationManager.transform(jsonData, transformationFunction);
		JsonNode jsonNode = mapper.readTree(result);
		Assert.assertEquals(12, jsonNode.get("keycount").intValue());
	}


	private static final String simpleMap =
"		function transform(dataString){"
		+" var GENERIC_DATA_STRING = JSON.parse(dataString);"
		+ "if(GENERIC_DATA_STRING != null){"
		+"		Object.keys(GENERIC_DATA_STRING).map("
		+ "			function(key, index) {"
		+ "				if(key === 'coord' || key === 'main'){ "
		+ "					GENERIC_DATA_STRING[key].newEntry = \"New Entry\";"
		+ "				}"
		+ "			});"
		+"	}"
		+ " return JSON.stringify(GENERIC_DATA_STRING);"
		+"}";


	@Test
	public void testMapTransformationExecution() throws ScriptException, IOException
	{
		transformationFunction = new TransformationFunction("1", simpleMap);
		String result = dataTransformationManager.transform(jsonData, transformationFunction);
		JsonNode jsonNode = mapper.readTree(result);

		Assert.assertEquals("New Entry", jsonNode.get("coord").get("newEntry").asText());
		Assert.assertEquals("New Entry", jsonNode.get("main").get("newEntry").asText());
	}

	@Test(expected = ScriptException.class)
	public void testInvalidTransformationExecution()
	throws ScriptException, IOException
	{
		transformationFunction = new TransformationFunction("2", "invalid Javascript Code");
		dataTransformationManager.transform(jsonData, transformationFunction);
	}

	private static final String infiniteLoop =
			"function transform(GENERIC_DATA_STRING){"
			+"    while(true) { ; }"
			+"};";

	@Test(expected = ScriptCPUAbuseException.class)
	public void testInfiniteLoopTransformationExecution()
	throws ScriptException, IOException
	{
		transformationFunction = new TransformationFunction("3", infiniteLoop);
		dataTransformationManager.transform(jsonData, transformationFunction);
	}


}