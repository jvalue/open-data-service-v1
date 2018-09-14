package org.jvalue.ods.api.processors;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;


public final class ProcessorReference {

	@NotNull private final String name;
	@NotNull private final Map<String, Object> arguments;


	@JsonCreator
	public ProcessorReference(
			@JsonProperty("name") String name,
			@JsonProperty("arguments") Map<String, Object> arguments) {

		this.name = name;
		this.arguments = arguments;
	}


	@Schema(example = "JsonSourceAdapter")
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
		ProcessorReference description = (ProcessorReference) other;
		return Objects.equal(name, description.name)
				&& Objects.equal(arguments, description.arguments);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(name, arguments);
	}


	public static final class Builder {

		private final String name;
		private final Map<String, Object> arguments = new HashMap<>();

		public Builder(String name) {
			this.name = name;
		}


		public Builder argument(String key, Object value) {
			this.arguments.put(key, value);
			return this;
		}


		public ProcessorReference build() {
			return new ProcessorReference(name, arguments);
		}

	}

}
