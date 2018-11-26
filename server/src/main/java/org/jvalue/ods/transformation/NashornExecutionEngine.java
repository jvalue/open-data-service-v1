package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
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

	private final Invocable sandboxedInvocable;
	private NashornSandbox nashornSandbox;

	private static String wrapperScript = "";
	private ObjectMapper objectMapper;

	@Inject
	public NashornExecutionEngine(@Assisted TransformationFunction transformationFunction) throws ScriptException {
		objectMapper = new ObjectMapper();
		InputStream resource = NashornExecutionEngine.class.getClassLoader().getResourceAsStream("js/NashornWrapper.js");
		try {
			wrapperScript = IOUtils.toString(resource);
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		initNashornSandbox();
		sandboxedInvocable = initInvocable(
			transformationFunction.getTransformationFunction(),
			transformationFunction.getReduceFunction());
	}


	private void initNashornSandbox() {
		nashornSandbox = NashornSandboxes.create();
		nashornSandbox.setMaxCPUTime(5000);
		nashornSandbox.allowNoBraces(true);
		nashornSandbox.setExecutor(Executors.newSingleThreadExecutor());
	}


	private Invocable initInvocable(String transformationFunction, String reduceFunction) throws ScriptException {
		//append custom transformation function to wrapper script
		String script = wrapperScript;
		if(transformationFunction != null){
			script += transformationFunction;
		}
		if(reduceFunction != null){
			script += reduceFunction;
		}

		//execute script
		nashornSandbox.eval(script);
		return nashornSandbox.getSandboxedInvocable();
	}

	@Override
	public ArrayNode execute(ObjectNode data, boolean query)
		throws ScriptException, IOException, NoSuchMethodException {

		ScriptObjectMirror o = (ScriptObjectMirror) sandboxedInvocable.invokeFunction(TRANSFORMATION_FUNCTION, data.toString(), query);
		ArrayNode result = new ArrayNode(JsonNodeFactory.instance);
		if(o == null) {
			throw new ScriptException("Return value of transform() is null.");
		}

		Collection<Object> values = o.values();
		for (Object obj : values) {
			result.add(objectMapper.readTree(obj.toString()));
		}

		return result;
	}



	@Override
	public ArrayNode reduce(ArrayNode resultSet)
		throws ScriptException, IOException, NoSuchMethodException {

		ArrayList<String> setAsList = convertArrayNodeToList(resultSet);
		Object o = sandboxedInvocable.invokeFunction(REDUCE_FUNCTION, setAsList);

		ArrayNode resultNode = new ArrayNode(JsonNodeFactory.instance);
		if (o == null) {
			throw new ScriptException("Return value of reduce() is null.");
		}
		resultNode.add(o.toString());

		return resultNode;
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
