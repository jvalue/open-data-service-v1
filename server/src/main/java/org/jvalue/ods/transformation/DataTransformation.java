package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;

public class DataTransformation
{
	private JsonNode data;
	private TransformationFunction transformationFunction;

	public DataTransformation(JsonNode data, TransformationFunction transformationFunction)
	{
		this.data = data;
		this.transformationFunction = transformationFunction;
	}

	public JsonNode transform(ExecutionEngine executionEngine)
	{
		return executionEngine.execute(data, transformationFunction);
	}
}
