package org.jvalue.ods.filter.reference;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import org.jvalue.ods.utils.Assert;

import java.util.Map;

public final class FilterReference {

	private final String name;
	private final Map<String, Object> arguments;

	@JsonCreator
	FilterReference(
		 	@JsonProperty("name") String name,
			@JsonProperty("arguments") Map<String, Object> arguments) {

		Assert.assertNotNull(name, arguments);
		this.name = name;
		this.arguments = arguments;
	}


	public String getName() {
		return name;
	}


	public Map<String, Object> getArguments() {
		return arguments;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof FilterReference)) return false;
		if (other == this) return true;
		FilterReference reference = (FilterReference) other;
		return Objects.equal(name, reference.name)
				&& Objects.equal(arguments, reference.arguments);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(name, arguments);
	}

}
