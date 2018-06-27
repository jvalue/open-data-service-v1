package org.jvalue.ods.transformation;

import javax.script.ScriptException;
import java.io.IOException;

public abstract class AbstractExecutionEngine implements ExecutionEngine
{

	protected static final String GENERIC_DATA_STRING = "genericDataString";

	@Override
	public abstract String execute(Object data, TransformationFunction transformationFunction)
	throws IOException, ScriptException;
}
