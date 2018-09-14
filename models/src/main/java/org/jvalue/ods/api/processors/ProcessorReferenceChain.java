package org.jvalue.ods.api.processors;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import javax.validation.constraints.NotNull;

public class ProcessorReferenceChain extends AbstractProcessorReferenceChain {

	@NotNull private final String id;

	public ProcessorReferenceChain(
			@JsonProperty("id") String id,
			@JsonProperty("processors") List<ProcessorReference> processors,
			@JsonProperty("executionInterval") ExecutionInterval executionInterval) {

		super(processors, executionInterval);
		this.id = id;
	}


	@Schema(hidden = true)
	public String getId() {
		return id;
	}


	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) return false;
		if (!(other instanceof ProcessorReferenceChain)) return false;
		ProcessorReferenceChain chain = (ProcessorReferenceChain) other;
		return Objects.equal(id, chain.id);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, super.hashCode());
	}

}
