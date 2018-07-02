package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public final class NashornExecutionEngineTest {

	private static ObjectNode jsonData;
	private static ExecutionEngine executionEngine;
	private static TransformationFunction transformationFunction;
	private static ObjectMapper mapper;
	private static String sampleData;


	@BeforeClass
	public static void initialize() throws IOException, URISyntaxException {
		mapper = new ObjectMapper();
		URL resource = NashornExecutionEngineTest.class.getClassLoader().getResource("js/SampleWeatherData");

		File sampleWeatherData = new File(resource.toURI());
		sampleData = FileUtils.readFileToString(sampleWeatherData);

		executionEngine = new NashornExecutionEngine();
		jsonData = (ObjectNode) mapper.readTree(sampleData);
	}


	private static final String simpleExtension =
			"function transform(doc){"
					+ "    if(doc.main != null){"
					+ "        doc.main.extension = \"This is an extension\";"
					+ "    }"
					+ "    return doc;"
					+ "};";


	@Test
	public void testExtensionTransformationExecution() throws ScriptException, IOException {
		transformationFunction = new TransformationFunction("1", simpleExtension);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);

		Assert.assertEquals("This is an extension", result.get("main").get("extension").asText());
	}


	private static final String simpleReduction =
			"		function transform(doc){"
					+ "if(doc != null){"
					+ "		var result = Object.keys(doc).reduce("
					+ "			function(previous, key) {"
					+ "				previous.keycount ++;"
					+ "				return previous;"
					+ "			}, {keycount: 0});"
					+ "			return result;"
					+ "		}"
					+ "}";


	@Test
	public void testReduceTransformationExecution() throws ScriptException, IOException {
		transformationFunction = new TransformationFunction("1", simpleReduction);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);
		Assert.assertEquals(12, result.get("keycount").intValue());
	}


	private static final String simpleMap =
			"		function transform(doc){"
					+ "if(doc != null){"
					+ "		Object.keys(doc).map("
					+ "			function(key, index) {"
					+ "				if(key === 'coord' || key === 'main'){ "
					+ "					doc[key].newEntry = \"New Entry\";"
					+ "				}"
					+ "			});"
					+ "	}"
					+ " return doc;"
					+ "}";


	@Test
	public void testMapTransformationExecution() throws ScriptException, IOException {
		transformationFunction = new TransformationFunction("1", simpleMap);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);

		Assert.assertEquals("New Entry", result.get("coord").get("newEntry").asText());
		Assert.assertEquals("New Entry", result.get("main").get("newEntry").asText());
	}


	@Test(expected = ScriptException.class)
	public void testInvalidTransformationExecution()
			throws ScriptException, IOException {
		transformationFunction = new TransformationFunction("2", "invalid Javascript Code");
		executionEngine.execute(jsonData, transformationFunction);
	}


	@Test(expected = ScriptException.class)
	public void testWrongFunctionSignatureTransformationExecution() throws ScriptException, IOException {
		transformationFunction = new TransformationFunction("10", "function test(hello){ return 1};");
		executionEngine.execute(jsonData, transformationFunction);
	}


	private static final String infiniteLoop =
			"function transform(dataString){"
					+ "    while(true) { ; }"
					+ "};";


	@Test(expected = ScriptCPUAbuseException.class)
	public void testInfiniteLoopTransformationExecution()
			throws ScriptException, IOException {
		transformationFunction = new TransformationFunction("3", infiniteLoop);
		executionEngine.execute(jsonData, transformationFunction);
	}


	private static final String javaClassAccess =
			"function transform(dataString){"
					+ "    while(true) { "
					+ "		var ArrayList = Java.type('java.util.ArrayList');"
					+ " }"
					+ "};";


	@Test(expected = RuntimeException.class)
	public void testAccessToJavaClassesTransformationExecution()
			throws ScriptException, IOException {
		transformationFunction = new TransformationFunction("3", javaClassAccess);
		executionEngine.execute(jsonData, transformationFunction);
	}
}