package org.jvalue.ods.api.views.generic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransformationFunctionDescription extends AbstractTransformationFunction {
	/**
	 * Create a new transformationFunction on the data.
	 *
	 * @param transformationFunction the transformation function written in JavaScript
	 */
	@JsonCreator
	public TransformationFunctionDescription(@JsonProperty("transformationFunction") String transformationFunction) {
		super(transformationFunction);
	}
}
