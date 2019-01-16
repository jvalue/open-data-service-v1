/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jvalue.commons.utils.Assert;
import org.jvalue.ods.processor.adapter.SourceAdapter;
import org.jvalue.ods.processor.adapter.SourceAdapterException;
import org.jvalue.ods.processor.filter.Filter;
import org.jvalue.ods.processor.filter.FilterException;

public final class ProcessorChain {

	private final SourceAdapter adapter;
	private final Filter<ObjectNode, ?> filterChain;


	public ProcessorChain(SourceAdapter adapter, Filter<ObjectNode, ?> filterChain) {
		Assert.assertNotNull(adapter, filterChain);
		this.adapter = adapter;
		this.filterChain = filterChain;
	}


	public void startProcessing() throws FilterException, SourceAdapterException {
		for (ObjectNode data : adapter) filterChain.filter(data);
		filterChain.onComplete();
	}

}
