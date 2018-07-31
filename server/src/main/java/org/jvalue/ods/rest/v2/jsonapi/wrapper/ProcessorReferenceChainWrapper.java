package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessorReferenceChainWrapper extends ProcessorReferenceChain implements JsonApiIdentifiable{

	private ProcessorReferenceChainWrapper(String id, List<ProcessorReference> processors, ExecutionInterval executionInterval) {
		super(id, processors, executionInterval);
	}


	@Override
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
