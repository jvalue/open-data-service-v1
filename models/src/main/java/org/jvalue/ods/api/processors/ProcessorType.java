package org.jvalue.ods.api.processors;


import java.util.EnumSet;
import java.util.Set;

public enum ProcessorType {

	SOURCE_ADAPTER,
	FILTER;

	static {
		SOURCE_ADAPTER.nextProcessorTypes = EnumSet.of(FILTER);
		FILTER.nextProcessorTypes = EnumSet.of(FILTER);
	}

	private Set<ProcessorType> nextProcessorTypes;

	public boolean isValidNextFilter(ProcessorType processorType) {
		return nextProcessorTypes.contains(processorType);
	}

}
