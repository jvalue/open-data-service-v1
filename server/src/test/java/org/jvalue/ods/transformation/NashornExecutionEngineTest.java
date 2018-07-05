package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;
import org.apache.commons.io.FileUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public final class NashornExecutionEngineTest {

	private static final String extension =
		"function transform(doc){"
			+ "    if(doc.main != null){"
			+ "        doc.main.extension = \"This is an extension\";"
			+ "    }"
			+ "    return doc;"
			+ "};";

	private static final String reduction =
		"function transform(doc){"
			+ "if(doc != null){"
			+ "		var result = Object.keys(doc).reduce("
			+ "			function(previous, key) {"
			+ "				previous.keycount ++;"
			+ "				return previous;"
			+ "			}, {keycount: 0});"
			+ "			return result;"
			+ "		}"
			+ "}";

	private static final String map =
		"function transform(doc){"
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

	private static final String infiniteLoop =
		"function transform(dataString){"
			+ "    while(true) { ; }"
			+ "};";

	private static final String javaClassAccess =
		"function transform(dataString){"
			+ "		var ArrayList = Java.type('java.util.ArrayList');"
			+ "};";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

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


	@Test
	public void testExtensionTransformationExecution() throws ScriptException, IOException, NoSuchMethodException {
		transformationFunction = new TransformationFunction("1", extension);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);

		Assert.assertEquals("This is an extension", result.get("main").get("extension").asText());
	}


	@Test
	public void testReduceTransformationExecution() throws ScriptException, IOException, NoSuchMethodException {
		transformationFunction = new TransformationFunction("1", reduction);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);
		Assert.assertEquals(12, result.get("keycount").intValue());
	}


	@Test
	public void testMapTransformationExecution() throws ScriptException, IOException, NoSuchMethodException {
		transformationFunction = new TransformationFunction("1", map);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);

		Assert.assertEquals("New Entry", result.get("coord").get("newEntry").asText());
		Assert.assertEquals("New Entry", result.get("main").get("newEntry").asText());
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
		throws ScriptException, IOException, NoSuchMethodException {

		thrown.expect(ScriptException.class);
		thrown.expectCause(CoreMatchers.isA(ScriptCPUAbuseException.class));

		transformationFunction = new TransformationFunction("3", infiniteLoop);
		executionEngine.execute(jsonData, transformationFunction);
	}


	@Test
	public void testAccessToJavaClassesTransformationExecution()
		throws ScriptException, IOException, NoSuchMethodException {

		thrown.expect(ScriptException.class);
		thrown.expectCause(CoreMatchers.isA(RuntimeException.class));

		transformationFunction = new TransformationFunction("3", javaClassAccess);
		executionEngine.execute(jsonData, transformationFunction);
	}
}
