package org.jvalue.ods.api.processors;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.List;

import javax.validation.constraints.NotNull;

public class ProcessorChainDescription {

	@NotNull private final List<ProcessorDescription> processors;
	@NotNull private final ExecutionInterval executionInterval;

	public ProcessorChainDescription(
			@JsonProperty("processors") List<ProcessorDescription> processors,
			@JsonProperty("executionInterval") ExecutionInterval executionInterval) {

		this.processors = processors;
		this.executionInterval = executionInterval;
	}


	public List<ProcessorDescription> getProcessors() {
		return processors;
	}


	public ExecutionInterval getExecutionInterval() {
		return executionInterval;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ProcessorChainDescription)) return false;
		if (other == this) return true;
		ProcessorChainDescription description = (ProcessorChainDescription) other;
		return Objects.equal(processors, description.processors)
				&& Objects.equal(executionInterval, description.executionInterval);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(processors, executionInterval);
	}

}
