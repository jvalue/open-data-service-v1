package org.jvalue.ods.processor;


import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jvalue.ods.processor.adapter.SourceAdapter;
import org.jvalue.ods.processor.adapter.SourceAdapterException;
import org.jvalue.ods.processor.filter.Filter;
import org.jvalue.ods.processor.filter.FilterException;
import org.jvalue.ods.utils.Assert;

public final class ProcessorChain {

	private final SourceAdapter adapter;
	private final Filter<ObjectNode, ?> filterChain;


	public ProcessorChain(SourceAdapter adapter, Filter<ObjectNode, ?> filterChain) {
		Assert.assertNotNull(adapter, filterChain);
		this.adapter = adapter;
		this.filterChain = filterChain;
	}


	public void startProcessing() throws FilterException, SourceAdapterException {
		for (ObjectNode data : adapter) {
			filterChain.filter(data);
		}
	}

}
