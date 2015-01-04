package org.jvalue.ods.api.processors;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.List;

import javax.validation.constraints.NotNull;

public final class ProcessorChain extends ProcessorChainDescription {

	@NotNull private final String id;

	public ProcessorChain(
			@JsonProperty("id") String id,
			@JsonProperty("processors") List<Processor> processors,
			@JsonProperty("executionInterval") ExecutionInterval executionInterval) {

		super(processors, executionInterval);
		this.id = id;
	}


	public String getId() {
		return id;
	}


	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) return false;
		if (!(other instanceof ProcessorChain)) return false;
		ProcessorChain chain = (ProcessorChain) other;
		return Objects.equal(id, chain.id);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, super.hashCode());
	}

}
