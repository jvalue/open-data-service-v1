package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.jvalue.ods.utils.JsonMapper;

import javax.script.ScriptException;
import java.io.IOException;
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


	@BeforeClass
	public static void initialize() throws IOException, URISyntaxException {

		Path path = Paths.get(NashornExecutionEngine.class.getClassLoader()
			.getResource("json/SampleWeatherData.json").toURI());
		String sampleData = new String(Files.readAllBytes(path));
		jsonData = (ObjectNode) JsonMapper.getInstance().readTree(sampleData);

		executionEngine = new NashornExecutionEngine();
	}


	@Test
	public void testExtensionTransformationExecution() throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {
		String extensionFunc = resourceFileToString("extension.js");
		transformationFunction = new TransformationFunction("1", extensionFunc);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);

		Assert.assertEquals("This is an extension", result.get("extension").asText());
	}


	@Test
	public void testReduceTransformationExecution() throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {
		String reductionFunc = resourceFileToString("reduction.js");
		transformationFunction = new TransformationFunction("1", reductionFunc);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);
		Assert.assertEquals(4, result.get("keycount").intValue());
	}


	@Test
	public void testMapTransformationExecution() throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {
		String mapFunc = resourceFileToString("map.js");
		transformationFunction = new TransformationFunction("1", mapFunc);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);

		Assert.assertEquals("RHEIN", result.get("water").get("shortname").asText());
		Assert.assertEquals("RHEIN", result.get("water").get("longname").asText());
	}


	@Test
	public void testConcatTransformationExecution() throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {
		String concatFunc = resourceFileToString("concat.js");
		transformationFunction = new TransformationFunction("1", concatFunc);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);

		Assert.assertEquals("13.929755188361455, 50.96458457915114", result.get("combinedCoords").asText());
	}


	@Test
	public void testFilterTransformationExecution() throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {
		String filterFunc = resourceFileToString("filter.js");
		transformationFunction = new TransformationFunction("1", filterFunc);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);
		System.out.println(result);
		Assert.assertTrue(result.get("stringValues").isArray());

		ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
		arrayNode.add("uuid");
		arrayNode.add("number");
		arrayNode.add("shortname");
		arrayNode.add("longname");
		arrayNode.add("agency");

		Assert.assertEquals(arrayNode, result.get("stringValues"));
	}
	

	@Test
	public void testArithmeticOperationsTransformationExecution() throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {
		String arithmeticFunc = resourceFileToString("arithmetic.js");
		transformationFunction = new TransformationFunction("1", arithmeticFunc);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);

		Assert.assertEquals(489.75, result.get("result").asDouble(), 0);
	}


	@Test(expected = ScriptException.class)
	public void testInvalidTransformationExecution()
		throws ScriptException, IOException, NoSuchMethodException {
		transformationFunction = new TransformationFunction("2", "invalid Javascript Code");
		executionEngine.execute(jsonData, transformationFunction);
	}


	@Test(expected = ScriptException.class)
	public void testWrongFunctionSignatureTransformationExecution()
		throws ScriptException, IOException, NoSuchMethodException {
		transformationFunction = new TransformationFunction("10", "function test(hello){ return 1};");
		executionEngine.execute(jsonData, transformationFunction);
	}


	@Test
	public void testInfiniteLoopTransformationExecution()
		throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {

		thrown.expect(ScriptException.class);
		thrown.expectCause(CoreMatchers.isA(ScriptCPUAbuseException.class));

		String infiniteLoopFunc = resourceFileToString("infiniteLoop.js");
		transformationFunction = new TransformationFunction("3", infiniteLoopFunc);
		executionEngine.execute(jsonData, transformationFunction);
	}


	@Test
	public void testAccessToJavaClassesTransformationExecution()
		throws ScriptException, IOException, NoSuchMethodException, URISyntaxException {

		thrown.expect(ScriptException.class);
		thrown.expectCause(CoreMatchers.isA(RuntimeException.class));

		String javaClassAccessFunc = resourceFileToString("javaClassAccess.js");
		transformationFunction = new TransformationFunction("3", javaClassAccessFunc);
		executionEngine.execute(jsonData, transformationFunction);
	}


	private String resourceFileToString(String fileName) throws URISyntaxException, IOException {
		Path path = Paths.get(getClass().getClassLoader()
			.getResource("transformation/" + fileName).toURI());

		return new String(Files.readAllBytes(path));
	}

}
