package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;

import javax.script.ScriptException;
import java.io.IOException;

public abstract class AbstractExecutionEngine implements ExecutionEngine {

	protected static final String JSON_STRING_VAR = "jsonString";


	@Override
	public abstract String execute(JsonNode data, TransformationFunction transformationFunction)
			throws IOException, ScriptException;
}
