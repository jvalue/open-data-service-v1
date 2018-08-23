package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.node.ObjectNode;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import org.apache.commons.io.IOUtils;
import org.jvalue.commons.utils.Log;
import org.jvalue.ods.utils.JsonMapper;

import javax.script.Invocable;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NashornExecutionEngine extends AbstractExecutionEngine {

	private NashornSandbox nashornSandbox;

	private static String wrapperScript = "";


	public NashornExecutionEngine() {
		InputStream resource = NashornExecutionEngine.class.getClassLoader().getResourceAsStream("js/NashornWrapper.js");
		try {
			wrapperScript = IOUtils.toString(resource);
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
	}


	private void initNashornSandbox() {
		nashornSandbox = NashornSandboxes.create();
		nashornSandbox.setMaxCPUTime(5000);
		nashornSandbox.allowNoBraces(true);
		nashornSandbox.setExecutor(Executors.newSingleThreadExecutor());
	}


	@Override
	public ObjectNode execute(ObjectNode data, TransformationFunction transformationFunction)
		throws ScriptException, IOException, NoSuchMethodException {
		initNashornSandbox();
		try {
			//append custom transformation function to wrapper script
			String script = wrapperScript + transformationFunction.getTransformFunction();

			//execute script
			nashornSandbox.eval(script);
			Invocable sandboxedInvocable = nashornSandbox.getSandboxedInvocable();
			String result = (String) sandboxedInvocable.invokeFunction(WRAPPER_FUNCTION, data.toString());
			return (ObjectNode) JsonMapper.getInstance().readTree(result);
		} finally {
			ExecutorService executor = nashornSandbox.getExecutor();
			executor.shutdown();
		}
	}
}
