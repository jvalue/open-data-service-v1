package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

public class TransformationManager
{
	private ExecutionEngine executionEngine;

	@Inject
	public TransformationManager(
			ExecutionEngine executionEngine)
	{
		this.executionEngine = executionEngine;
	}

	public JsonNode executeTransformation(DataTransformation dataTransformation){
		return dataTransformation.transform(executionEngine);
	}

}
