package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.FileUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class NashornExecutionEngine implements ExecutionEngine
{

	private final ScriptEngine nashornEngine;

	private final ObjectMapper mapper;

	private File jsonUtilJs = null;

	private static final String scriptEntryFunction = "transformationWrapper";

	public NashornExecutionEngine()
	{
		mapper = new ObjectMapper();
		ScriptEngineManager factory = new ScriptEngineManager();
		nashornEngine = factory.getEngineByName("nashorn");

		URL resource = this.getClass().getClassLoader().getResource("js/JsonUtil.js");
		try
		{
			jsonUtilJs = new File(resource.toURI());
		}
		catch (URISyntaxException e)
		{
			//TODO logger
			e.printStackTrace();
		}
	}

	@Override
	public JsonNode execute(JsonNode data, TransformationFunction transformationFunction)
	{
		Invocable invocable = (Invocable) nashornEngine;
		try
		{

			//append custom transformation function to wrapper script
			String script = FileUtils.readFileToString(jsonUtilJs);
			script += transformationFunction.getTransformFunction();

			//execute script
			nashornEngine.eval(script);
			String result =  (String) invocable.invokeFunction(scriptEntryFunction, data.toString());

			JsonNode dataResult = mapper.readTree(result);
			return dataResult;
		}
		catch (ScriptException | NoSuchMethodException | IOException e)
		{
			ObjectNode jNode = mapper.createObjectNode();
			jNode.put("error", e.getMessage());
			return jNode;
		}
	}
}
