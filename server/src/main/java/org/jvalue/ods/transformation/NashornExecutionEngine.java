package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.IOUtils;
import org.jvalue.commons.utils.Log;
import org.jvalue.ods.api.views.generic.TransformationFunction;

import javax.script.Invocable;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NashornExecutionEngine extends AbstractExecutionEngine {

	private NashornSandbox nashornSandbox;

	private static String wrapperScript = "";
	private ObjectMapper objectMapper;


	public NashornExecutionEngine() {
		objectMapper = new ObjectMapper();
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


	private Invocable initInvocable(String function) throws ScriptException {
		//append custom transformation function to wrapper script
		String script = wrapperScript + function;

		//execute script
		nashornSandbox.eval(script);
		return nashornSandbox.getSandboxedInvocable();
	}

	@Override
	public ArrayNode execute(ObjectNode data, TransformationFunction transformationFunction, boolean query)
		throws ScriptException, IOException, NoSuchMethodException {
		initNashornSandbox();
		try {
			Invocable sandboxedInvocable = initInvocable(transformationFunction.getTransformationFunction());
			ScriptObjectMirror o = (ScriptObjectMirror) sandboxedInvocable.invokeFunction(TRANSFORMATION_FUNCTION, data.toString(), query);
			ArrayNode result = new ArrayNode(JsonNodeFactory.instance);
			if(o != null){
				Collection<Object> values = o.values();
				for (Object obj : values) {
					result.add(objectMapper.readTree(obj.toString()));
				}
			}

			return result;
		} finally {
			ExecutorService executor = nashornSandbox.getExecutor();
			executor.shutdown();
		}
	}



	@Override
	public ArrayNode reduce(ArrayNode resultSet, TransformationFunction transformationFunction)
		throws ScriptException, IOException, NoSuchMethodException {
		initNashornSandbox();
		try {
			Invocable sandboxedInvocable = initInvocable(transformationFunction.getReduceFunction());
			ArrayList<String> setAsList = convertArrayNodeToList(resultSet);
			Object o = sandboxedInvocable.invokeFunction(REDUCE_FUNCTION, setAsList);

			ArrayNode resultNode = new ArrayNode(JsonNodeFactory.instance);
			if (o != null) {
				resultNode.add(o.toString());
			}

			return resultNode;
		} finally {
			ExecutorService executor = nashornSandbox.getExecutor();
			executor.shutdown();
		}
	}


	private ArrayList<String> convertArrayNodeToList(ArrayNode set) {
		ArrayList<String> list = new ArrayList<>();
		Iterator<JsonNode> elements = set.elements();
		while (elements.hasNext()) {
			JsonNode next = elements.next();
			list.add(next.toString());
		}
		return list;
	}
}
