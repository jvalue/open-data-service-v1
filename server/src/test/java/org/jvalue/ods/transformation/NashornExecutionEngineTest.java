package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;
import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.jvalue.commons.utils.Log;
import org.jvalue.ods.api.views.generic.TransformationFunction;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class NashornExecutionEngineTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static ObjectNode jsonData;
	private static ExecutionEngine executionEngine;
	private static TransformationFunction transformationFunction;
	private static String sampleData;


	@BeforeClass
	public static void initialize() throws IOException, URISyntaxException {

		InputStream resource = NashornExecutionEngine.class.getClassLoader().getResourceAsStream("json/SampleWeatherData.json");
		try {
			sampleData = IOUtils.toString(resource);
		}catch (IOException e){
			Log.error(e.getMessage());
		}

		executionEngine = new NashornExecutionEngine();
		jsonData = (ObjectNode) new ObjectMapper().readTree(sampleData);
	}


	@Test
	public void testExtensionTransformationExecution() throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {
		String extensionFunc = resourceFileToString("extension.js");
		transformationFunction = new TransformationFunction("1", extensionFunc,null);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction, false);

		Assert.assertEquals("This is an extension", result.get(0).get("extension").asText());
	}


	@Test
	public void testReduceTransformationExecution() throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {
		String reductionFunc = resourceFileToString("reduction.js");
		transformationFunction = new TransformationFunction("1", reductionFunc,null);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction, false);
		Assert.assertEquals(4, result.get(0).get("keycount").intValue());
	}


	@Test
	public void testMapTransformationExecution() throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {
		String mapFunc = resourceFileToString("map.js");
		transformationFunction = new TransformationFunction("1", mapFunc,null);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction, false);

		Assert.assertEquals("RHEIN", result.get(0).get("water").get("shortname").asText());
		Assert.assertEquals("RHEIN", result.get(0).get("water").get("longname").asText());
	}

	@Test
	public void testConcatTransformationExecution() throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {
		String concatFunc = resourceFileToString("concat.js");
		transformationFunction = new TransformationFunction("1", concatFunc,null);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction, false);

		Assert.assertEquals("13.929755188361455, 50.96458457915114", result.get(0).get("combinedCoords").asText());
	}

	@Test
	public void testFilterTransformationExecution() throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {
		String filterFunc = resourceFileToString("filter.js");
		transformationFunction = new TransformationFunction("1", filterFunc,null);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction, false);

		Assert.assertTrue(result.get(0).get("stringValues").isArray());

		ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
		arrayNode.add("uuid");
		arrayNode.add("number");
		arrayNode.add("shortname");
		arrayNode.add("longname");
		arrayNode.add("agency");

		Assert.assertEquals(arrayNode, result.get(0).get("stringValues"));
	}

	@Test
	public void testArithmeticOperationsTransformationExecution() throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {
		String arithmeticFunc = resourceFileToString("arithmetic.js");
		transformationFunction = new TransformationFunction("1", arithmeticFunc,null);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction, false);

		Assert.assertEquals(489.75, result.get(0).get("result").asDouble(), 0);
	}

	@Test(expected = ScriptException.class)
	public void testInvalidTransformationExecution()
		throws ScriptException, IOException, NoSuchMethodException {
		transformationFunction = new TransformationFunction("2", "invalid Javascript Code",null);
		executionEngine.execute(jsonData, transformationFunction, false);
	}


	@Test(expected = ScriptException.class)
	public void testWrongFunctionSignatureTransformationExecution()
		throws ScriptException, IOException, NoSuchMethodException {
		transformationFunction = new TransformationFunction("10", "function test(hello){ return 1};",null);
		executionEngine.execute(jsonData, transformationFunction, false);
	}


	@Test
	public void testInfiniteLoopTransformationExecution()
		throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {

		thrown.expect(ScriptException.class);
		thrown.expectCause(CoreMatchers.isA(ScriptCPUAbuseException.class));

		String infiniteLoopFunc = resourceFileToString("infiniteLoop.js");
		transformationFunction = new TransformationFunction("3", infiniteLoopFunc,null);
		executionEngine.execute(jsonData, transformationFunction, false);
	}


	@Test
	public void testAccessToJavaClassesTransformationExecution()
		throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {

		thrown.expect(ScriptException.class);
		thrown.expectCause(CoreMatchers.isA(RuntimeException.class));

		String javaClassAccessFunc = resourceFileToString("javaClassAccess.js");
		transformationFunction = new TransformationFunction("3", javaClassAccessFunc,null);
		executionEngine.execute(jsonData, transformationFunction, false);
	}

	private String resourceFileToString(String fileName) throws URISyntaxException, IOException {
		Path path = Paths.get(getClass().getClassLoader()
			.getResource("transformation/" + fileName).toURI());

		return new String(Files.readAllBytes(path));
	}
}
