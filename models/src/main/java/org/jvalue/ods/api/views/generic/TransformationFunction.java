package org.jvalue.ods.api.views.generic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import org.jvalue.ods.api.views.DataView;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransformationFunction extends AbstractTransformationFunction implements DataView {
	@NotNull
	private final String id;

	@NotNull
	private final String transformationFunction;


	/**
	 * Create a new transformationFunction on the data.
	 *
	 * @param id                the id of the transformation function
	 * @param transformationFunction the transformation function written in JavaScript
	 */
	@JsonCreator
	public TransformationFunction(
		@JsonProperty("id") String id,
		@JsonProperty("transformationFunction") String transformationFunction) {
		super(transformationFunction);

		this.id = id;
		this.transformationFunction = transformationFunction;
	}


	public String getId() {
		return id;
	}


	public String getTransformationFunction() {
		return transformationFunction;
	}


	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) return false;
		if (!(other instanceof TransformationFunction)) return false;
		TransformationFunction transformationFunction = (TransformationFunction) other;
		return Objects.equal(id, transformationFunction.id);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, super.hashCode());
	}
}
