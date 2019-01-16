/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.api.processors;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

public final class ProcessorReferenceChainDescription extends AbstractProcessorReferenceChain {

	public ProcessorReferenceChainDescription(
			@JsonProperty("processors") List<ProcessorReference> processors,
			@JsonProperty("executionInterval") ExecutionInterval executionInterval) {

		super(processors, executionInterval);
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
