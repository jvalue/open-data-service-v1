package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.script.ScriptException;
import java.io.IOException;

public interface ExecutionEngine {
	ObjectNode execute(ObjectNode data, TransformationFunction transformationFunction) throws IOException,
		ScriptException, NoSuchMethodException;
}
