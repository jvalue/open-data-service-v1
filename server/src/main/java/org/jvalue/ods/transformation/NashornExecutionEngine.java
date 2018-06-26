package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.FileUtils;
import org.jvalue.commons.utils.Log;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NashornExecutionEngine implements ExecutionEngine
{

	private final NashornSandbox nashornSandbox;
	private final ObjectMapper mapper = new ObjectMapper();
	private String wrapperScript = "";

	private static final String scriptGlobalJsonVar = "injectedJsonStr";

	public NashornExecutionEngine()
	{
		//configure the nashorn sandbox
		nashornSandbox = NashornSandboxes.create();
		nashornSandbox.setMaxCPUTime(5000);
		nashornSandbox.allowNoBraces(true);

		URL resource = this.getClass().getClassLoader().getResource("js/JsonUtil.js");

		try
		{
			File jsonUtilJs = new File(resource.toURI());
			wrapperScript = FileUtils.readFileToString(jsonUtilJs);
		}
		catch (URISyntaxException e)
		{
			Log.info("Resource URI failure: "+ e.getMessage() );
			e.printStackTrace();
		}
		catch (IOException e)
		{
			Log.info("IO Exception: "+ e.getMessage() );
			e.printStackTrace();
		}
	}

	@Override
	public JsonNode execute(JsonNode data, TransformationFunction transformationFunction)
	throws ScriptException, IOException
	{
		JsonNode dataResult;
		try
		{
			nashornSandbox.setExecutor(Executors.newSingleThreadExecutor());

			//append custom transformation function to wrapper script
			String script = wrapperScript + transformationFunction.getTransformFunction();

			//execute script
			nashornSandbox.inject(scriptGlobalJsonVar, data.toString());
			String result = (String) nashornSandbox.eval(script);
			dataResult = mapper.readTree(result);

			return dataResult;
		}
		finally
		{
			ExecutorService executor = nashornSandbox.getExecutor();
			executor.shutdown();
		}
	}
}
