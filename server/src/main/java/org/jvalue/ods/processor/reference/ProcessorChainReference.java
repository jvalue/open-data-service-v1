package org.jvalue.ods.processor.reference;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.utils.Assert;

import java.util.List;


public final class ProcessorChainReference extends CouchDbDocument {

	private final String processorChainId;
	private final List<ProcessorReference> processors;
	private final ExecutionInterval executionInterval;

	@JsonCreator
	ProcessorChainReference(
			@JsonProperty("processorChainId") String processorChainId,
			@JsonProperty("processors") List<ProcessorReference> processors,
			@JsonProperty("executionInterval") ExecutionInterval executionInterval) {

		Assert.assertNotNull(processorChainId, processors);
		this.processorChainId = processorChainId;
		this.processors = processors;
		this.executionInterval = executionInterval;
	}


	public String getProcessorChainId() {
		return processorChainId;
	}


	public List<ProcessorReference> getProcessors() {
		return processors;
	}


	public ExecutionInterval getExecutionInterval() {
		return executionInterval;
	}

}
