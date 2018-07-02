package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import org.apache.commons.io.FileUtils;
import org.jvalue.commons.utils.Log;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NashornExecutionEngine extends AbstractExecutionEngine {

	private NashornSandbox nashornSandbox;

	private static String wrapperScript = "";


	public NashornExecutionEngine() {
		URL resource = NashornExecutionEngine.class.getClassLoader().getResource("js/JsonUtil.js");
		File wrapperScriptJs;
		try {
			wrapperScriptJs = new File(resource.toURI());
			wrapperScript = FileUtils.readFileToString(wrapperScriptJs);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
	}


	private void initNashornSandbox() {
		//configure the nashorn sandbox
		nashornSandbox = NashornSandboxes.create();
		nashornSandbox.setMaxCPUTime(5000);
		nashornSandbox.allowNoBraces(true);
		nashornSandbox.setExecutor(Executors.newSingleThreadExecutor());
	}


	@Override
	public String execute(JsonNode data, TransformationFunction transformationFunction)
			throws ScriptException, IOException {
		initNashornSandbox();
		try {
			//append custom transformation function to wrapper script
			String script = wrapperScript + transformationFunction.getTransformFunction();

			//execute script
			nashornSandbox.inject(JSON_STRING_VAR, data.toString());
			String result = (String) nashornSandbox.eval(script);
			return result;
		} finally {
			ExecutorService executor = nashornSandbox.getExecutor();
			executor.shutdown();
		}
	}
}
