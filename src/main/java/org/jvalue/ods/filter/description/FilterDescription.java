package org.jvalue.ods.filter.description;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import org.jvalue.ods.utils.Assert;

import java.util.Map;

public final class FilterDescription {

	private final String name;
	private final FilterType type;
	private final Map<String, Class<?>> argumentTypes;

	@JsonCreator
	FilterDescription(
			@JsonProperty("name") String name,
			@JsonProperty("type") FilterType type,
			@JsonProperty("argumentTypes") Map<String, Class<?>> argumentTypes) {

		Assert.assertNotNull(name, type, argumentTypes);
		this.name = name;
		this.type = type;
		this.argumentTypes = argumentTypes;
	}


	public String getName() {
		return name;
	}


	public FilterType getType() {
		return type;
	}


	public Map<String, Class<?>> getArgumentTypes() {
		return argumentTypes;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof FilterDescription)) return false;
		if (other == this) return true;
		FilterDescription reference = (FilterDescription) other;
		return Objects.equal(name, reference.name)
				&& Objects.equal(type, reference.type)
				&& Objects.equal(argumentTypes, reference.argumentTypes);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(name, type, argumentTypes);
	}

}
