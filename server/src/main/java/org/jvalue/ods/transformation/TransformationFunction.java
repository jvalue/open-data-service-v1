/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.transformation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;

public class TransformationFunction {
	@NotNull
	private final String id;

	@NotNull
	private final String transformFunction;


	/**
	 * Create a new transformationFunction on the data.
	 *
	 * @param id                the id of the transformation function
	 * @param transformFunction the transformation function written in JavaScript
	 */
	@JsonCreator
	public TransformationFunction(
		@JsonProperty("id") String id,
		@JsonProperty("transformFunction") String transformFunction) {

		this.id = id;
		this.transformFunction = transformFunction;
	}


	public String getId() {
		return id;
	}


	public String getTransformFunction() {
		return transformFunction;
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
