/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.admin.monitoring;


import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;

public final class MonitoringModule extends AbstractModule {

	private final MetricRegistry registry;


	public MonitoringModule(MetricRegistry registry) {
		this.registry = registry;
	}


	@Override
	protected void configure() {
		bind(MetricRegistry.class).toInstance(registry);
	}

}
