package org.jvalue.ods.api.processors;


import com.google.common.base.Objects;
import org.jvalue.commons.EntityBase;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

abstract class AbstractProcessorReferenceChain {

	@Valid @NotNull private final List<ProcessorReference> processors;
	@Valid private final ExecutionInterval executionInterval;

	protected AbstractProcessorReferenceChain(
			List<ProcessorReference> processors,
			ExecutionInterval executionInterval) {

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
		if (other == null || !(other instanceof AbstractProcessorReferenceChain)) return false;
		if (other == this) return true;
		AbstractProcessorReferenceChain description = (AbstractProcessorReferenceChain) other;
		return Objects.equal(processors, description.processors)
				&& Objects.equal(executionInterval, description.executionInterval);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(processors, executionInterval);
	}

}
