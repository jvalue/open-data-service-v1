package org.jvalue.ods.api.processors;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ProcessorReferenceChainDescription {

	@Valid @NotNull private final List<ProcessorReference> processors;
	@Valid private final ExecutionInterval executionInterval;

	public ProcessorReferenceChainDescription(
			@JsonProperty("processors") List<ProcessorReference> processors,
			@JsonProperty("executionInterval") ExecutionInterval executionInterval) {

		this.processors = processors;
		this.executionInterval = executionInterval;
	}


	public List<ProcessorReference> getProcessors() {
		return processors;
	}


	public ExecutionInterval getExecutionInterval() {
		return executionInterval;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ProcessorReferenceChainDescription)) return false;
		if (other == this) return true;
		ProcessorReferenceChainDescription description = (ProcessorReferenceChainDescription) other;
		return Objects.equal(processors, description.processors)
				&& Objects.equal(executionInterval, description.executionInterval);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(processors, executionInterval);
	}


	public static final class Builder {

		private final List<ProcessorReference> processors = new LinkedList<>();
		private final ExecutionInterval executionInterval;

		public Builder(ExecutionInterval executionInterval) {
			this.executionInterval = executionInterval;
		}


		public Builder processor(ProcessorReference processor) {
			this.processors.add(processor);
			return this;
		}


		public ProcessorReferenceChainDescription build() {
			return new ProcessorReferenceChainDescription(processors, executionInterval);
		}

	}
}
