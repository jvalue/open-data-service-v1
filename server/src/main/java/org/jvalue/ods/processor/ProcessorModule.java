/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.jvalue.ods.processor.adapter.AdapterModule;
import org.jvalue.ods.processor.filter.FilterModule;

public final class ProcessorModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new AdapterModule());
		install(new FilterModule());
		bind(ProcessorChainManager.class).in(Singleton.class);
		bind(ProcessorChainFactory.class).in(Singleton.class);
	}

}
