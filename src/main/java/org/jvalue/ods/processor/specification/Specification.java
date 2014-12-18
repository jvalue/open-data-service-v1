package org.jvalue.ods.processor.specification;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import org.jvalue.ods.utils.Assert;

import java.util.Map;

public final class Specification {

	private final String name;
	private final ProcessorType type;
	private final Map<String, Class<?>> argumentTypes;

	@JsonCreator
	Specification(
			@JsonProperty("name") String name,
			@JsonProperty("type") ProcessorType type,
			@JsonProperty("argumentTypes") Map<String, Class<?>> argumentTypes) {

		Assert.assertNotNull(name, type, argumentTypes);
		this.name = name;
		this.type = type;
		this.argumentTypes = argumentTypes;
	}


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
