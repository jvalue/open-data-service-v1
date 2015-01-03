package org.jvalue.ods.processor.reference;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import org.jvalue.ods.utils.Assert;

import java.util.Map;

public final class ProcessorReference {

	private final String name;
	private final Map<String, Object> arguments;

	@JsonCreator
	ProcessorReference(
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
		if (other == null || !(other instanceof ProcessorReference)) return false;
		if (other == this) return true;
		ProcessorReference reference = (ProcessorReference) other;
		return Objects.equal(name, reference.name)
				&& Objects.equal(arguments, reference.arguments);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(name, arguments);
	}

}
