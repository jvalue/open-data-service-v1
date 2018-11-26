package org.jvalue.ods.transformation;

import org.jvalue.ods.api.views.generic.TransformationFunction;

import javax.script.ScriptException;

public class TestExecutionEngineFactory implements ExecutionEngineFactory {

	@Override
	public ExecutionEngine createExecutionEngine(TransformationFunction transformationFunction) throws ScriptException {
		return new NashornExecutionEngine(transformationFunction);
	}
}
