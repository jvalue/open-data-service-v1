package org.jvalue.ods.api.processors;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import org.jvalue.commons.EntityBase;

import java.util.List;

import javax.validation.constraints.NotNull;

public final class ProcessorReferenceChain extends AbstractProcessorReferenceChain implements EntityBase {

	@SerializedName("_id")
	@NotNull private final String id;

	public ProcessorReferenceChain(
			@JsonProperty("id") String id,
			@JsonProperty("processors") List<ProcessorReference> processors,
			@JsonProperty("executionInterval") ExecutionInterval executionInterval) {

		super(processors, executionInterval);
		this.id = id;
	}

	@Override
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
