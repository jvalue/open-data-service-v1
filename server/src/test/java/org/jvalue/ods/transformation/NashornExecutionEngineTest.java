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

public final class NashornExecutionEngineTest {

	private static final String extension =
		"function transform(doc){"
			+ "    if(doc != null){"
			+ "        doc.extension = \"This is an extension\";"
			+ "    }"
			+ "    return doc;"
			+ "};";

	private static final String reduction =
		"function transform(doc){"
			+ "if(doc != null){"
			+ "		return doc.timeseries[0].characteristicValues.reduce("
			+ "			function(previous, key) {"
			+ "				previous.keycount ++;"
			+ "				return previous;"
			+ "			}, {keycount: 0});"
			+ "		}"
			+ "}";

	private static final String map =
		"function transform(doc){"
			+ "if(doc != null){"
			+ "		Object.keys(doc.water).map("
			+ "			function(key, index) {"
			+ 				"doc.water[key] = \"RHEIN\""
			+ "			});"
			+ "		}"
			+ "     return doc;"
			+ "}";

	private static final String filter =
		"function transform(doc){"
			+ "var new_doc = {};"
			+ "if(doc != null){"
			+ "		new_doc.stringValues = Object.keys(doc).filter("
			+ "			function(key) {"
			+ 				"return typeof doc[key] === 'string'"
			+ "			});"
			+ "		}"
			+ "     return new_doc;"
			+ "}";

	private static final String concatStrings =
		"function transform(doc){"
			+ "if(doc != null){"
			+ "		doc.combinedCoords = doc.longitude + ', ' + doc.latitude"
			+ "	}" +
			"   return doc;"
			+ "}";

	private static final String arithmeticOperations =
		"function sum(valueArray){"
			+"	return valueArray.reduce("
			+"		function(previous,element){"
			+"   		return previous + element;"
			+"		}, 0);"
			+"}"
			+
		"function avarage(doubleValueArray){"
			+"	return sum(doubleValueArray) / doubleValueArray.length;"
			+"}"
			+
		"function transform(doc){"
			+ "var values;"
			+ "if(doc != null){"
			+ "		values = doc.timeseries[0].characteristicValues.reduce("
			+ "			function(previous, element) {"
			+ "				previous.push(element.value);"
			+ "				return previous;"
			+ "			}, []);"
			+ "		}"
			+ "		return {'result' : avarage(values)}"
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

		InputStream resource = NashornExecutionEngine.class.getClassLoader().getResourceAsStream("json/SampleWeatherData.json");
		try {
			sampleData = IOUtils.toString(resource);
		}catch (IOException e){
			Log.error(e.getMessage());
		}

		executionEngine = new NashornExecutionEngine();
		jsonData = (ObjectNode) mapper.readTree(sampleData);
	}


	@Test
	public void testExtensionTransformationExecution() throws ScriptException, IOException, NoSuchMethodException {
		transformationFunction = new TransformationFunction("1", extension);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);

		Assert.assertEquals("This is an extension", result.get("extension").asText());
	}


	@Test
	public void testReduceTransformationExecution() throws ScriptException, IOException, NoSuchMethodException {
		transformationFunction = new TransformationFunction("1", reduction);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);
		Assert.assertEquals(4, result.get("keycount").intValue());
	}


	@Test
	public void testMapTransformationExecution() throws ScriptException, IOException, NoSuchMethodException {
		transformationFunction = new TransformationFunction("1", map);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);

		Assert.assertEquals("RHEIN", result.get("water").get("shortname").asText());
		Assert.assertEquals("RHEIN", result.get("water").get("longname").asText());
	}

	@Test
	public void testConcatTransformationExecution() throws ScriptException, IOException, NoSuchMethodException {
		transformationFunction = new TransformationFunction("1", concatStrings);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);

		Assert.assertEquals("13.929755188361455, 50.96458457915114", result.get("combinedCoords").asText());
	}

	@Test
	public void testFilterTransformationExecution() throws ScriptException, IOException, NoSuchMethodException {
		transformationFunction = new TransformationFunction("1", filter);
		JsonNode result = executionEngine.execute(jsonData, transformationFunction);

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
	public void testArithmeticOperationsTransformationExecution() throws ScriptException, IOException, NoSuchMethodException {
		transformationFunction = new TransformationFunction("1", arithmeticOperations);
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
