package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import javax.script.ScriptException;
import java.io.IOException;

public class DataTransformationManager {
	private final ExecutionEngine executionEngine;


	@Inject
	public DataTransformationManager(ExecutionEngine executionEngine) {
		this.executionEngine = executionEngine;
	}


	public ObjectNode transform(ObjectNode data, TransformationFunction transformationFunction)
			throws ScriptException, IOException {
		return executionEngine.execute(data, transformationFunction);
	}
}
