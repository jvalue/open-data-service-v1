package org.jvalue.ods.transformation;

import com.google.inject.name.Named;
import org.jvalue.ods.api.views.generic.TransformationFunction;

import javax.script.ScriptException;

public interface ExecutionEngineFactory {
	static final String
		EXECUTION_ENGINE_IMPLEMENTATION = "ExecutionEngine";

	@Named(EXECUTION_ENGINE_IMPLEMENTATION)
	public ExecutionEngine createExecutionEngine(TransformationFunction transformationFunction) throws ScriptException;
}
