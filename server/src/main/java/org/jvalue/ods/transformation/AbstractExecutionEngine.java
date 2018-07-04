package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.script.ScriptException;
import java.io.IOException;

public abstract class AbstractExecutionEngine implements ExecutionEngine {

	// used as a variable to be injected into the script code
	// as random as possible variable so the user does not use the variable by accident
	protected static final String JSON_STRING_VAR = "mvrzesmadgnxpxqhwllv";


	@Override
	public abstract ObjectNode execute(ObjectNode data, TransformationFunction transformationFunction)
		throws IOException, ScriptException;
}
