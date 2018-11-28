package org.jvalue.ods.api.views.generic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;

public abstract class AbstractTransformationFunction {

	@NotNull
	private final String transformationFunction;

	private final String reduceFunction;


	/**
	 * Create a new transformationFunction on the data.
	 *
	 * @param transformFunction the transformation function written in JavaScript
	 * @param reduceFunction optional reduce function written in JavaScript
	 */
	@JsonCreator
	public AbstractTransformationFunction(
		String transformFunction, String reduceFunction) {

		this.transformationFunction = transformFunction;
		this.reduceFunction = reduceFunction;
	}

	public String getTransformationFunction() {
		return transformationFunction;
	}

	public String getReduceFunction() {
		return reduceFunction;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof AbstractTransformationFunction)) return false;
		if (other == this) return true;
		AbstractTransformationFunction view = (AbstractTransformationFunction) other;
		return Objects.equal(transformationFunction, view.transformationFunction);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(transformationFunction);
	}
}
