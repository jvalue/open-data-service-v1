package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "processorReferenceChainData")
public class ProcessorReferenceChainWrapper implements JsonApiIdentifiable{

	private final ProcessorReferenceChain processorReferenceChain;

	private ProcessorReferenceChainWrapper(String id, List<ProcessorReference> processors, ExecutionInterval executionInterval) {
		processorReferenceChain = new ProcessorReferenceChain(id, processors, executionInterval);
	}


	@Schema(name = "attributes", required = true)
	public ProcessorReferenceChain getProcessorReferenceChain() {
		return processorReferenceChain;
	}


	@Override
	@Schema(example = "mainFilter", required = true)
	public String getId() {
		return processorReferenceChain.getId();
	}


	@Override
	@Schema(allowableValues = "ProcessorReferenceChain", required = true)
	public String getType() {
		return ProcessorReferenceChain.class.getSimpleName();
	}


	public static ProcessorReferenceChainWrapper from(ProcessorReferenceChain chain) {
		return new ProcessorReferenceChainWrapper(chain.getId(), chain.getProcessors(), chain.getExecutionInterval());
	}


	public static Collection<ProcessorReferenceChainWrapper> fromCollection(Collection<ProcessorReferenceChain> chains) {
		return chains.stream()
			.map(ProcessorReferenceChainWrapper::from)
			.collect(Collectors.toList());
	}
}
