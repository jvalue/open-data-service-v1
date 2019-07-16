/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.api.processors;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class Specification {

	@NotNull private final String name;
	@NotNull private final ProcessorType type;
	@NotNull private final Map<String, Class<?>> argumentTypes;

	@JsonCreator
	public Specification(
			@JsonProperty("name") String name,
			@JsonProperty("type") ProcessorType type,
			@JsonProperty("argumentTypes") Map<String, Class<?>> argumentTypes) {

		this.name = name;
		this.type = type;
		this.argumentTypes = argumentTypes;
	}


	@Schema(example = "JsonSourceAdapter")
	public String getName() {
		return name;
	}


	public ProcessorType getType() {
		return type;
	}


	public Map<String, Class<?>> getArgumentTypes() {
		return argumentTypes;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Specification)) return false;
		if (other == this) return true;
		Specification reference = (Specification) other;
		return Objects.equal(name, reference.name)
				&& Objects.equal(type, reference.type)
				&& Objects.equal(argumentTypes, reference.argumentTypes);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(name, type, argumentTypes);
	}

}
