package org.jvalue.ods.transformation;

import javax.script.ScriptException;
import java.io.IOException;

public interface ExecutionEngine
{
	String execute(Object data, TransformationFunction transformationFunction) throws  IOException,
			ScriptException;
}
