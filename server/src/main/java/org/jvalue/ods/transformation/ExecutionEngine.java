package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;

public interface ExecutionEngine
{
	JsonNode execute(JsonNode data, TransformationFunction transformationFunction);
}
