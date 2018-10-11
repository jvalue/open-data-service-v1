package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.IOUtils;
import org.jvalue.commons.utils.Log;
import org.jvalue.ods.api.views.generic.TransformationFunction;
import org.jvalue.ods.utils.JsonMapper;

import javax.script.Invocable;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
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
	public ArrayNode execute(ObjectNode data, TransformationFunction transformationFunction, boolean query)
		throws ScriptException, IOException, NoSuchMethodException {
		initNashornSandbox();
		try {
			//append custom transformation function to wrapper script
			String script = wrapperScript + transformationFunction.getTransformationFunction();

			//execute script
			nashornSandbox.eval(script);
			Invocable sandboxedInvocable = nashornSandbox.getSandboxedInvocable();
			ScriptObjectMirror o = (ScriptObjectMirror) sandboxedInvocable.invokeFunction(WRAPPER_FUNCTION, data.toString(), query);
			Collection<Object> values =  o.values();

			//add every call of emit() to the result set
			ArrayNode result = new ArrayNode(JsonNodeFactory.instance);
			for (Object obj : values){
				result.add(JsonMapper.getInstance().readTree(obj.toString()));
			}

			return result;
		} finally {
			ExecutorService executor = nashornSandbox.getExecutor();
			executor.shutdown();
		}
	}
}
