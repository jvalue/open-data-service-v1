/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.pegelalarm;

import com.codahale.metrics.health.HealthCheck;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.processor.ProcessorChainManager;

import javax.inject.Inject;

/**
 * Asserts that a {@link org.jvalue.ods.api.sources.DataSource} with the id 'pegelalarm' exists.
 */
public class FilterChainHealthCheck extends HealthCheck implements Constants {

	private final DataSourceManager dataSourceManager;
	private final ProcessorChainManager processorChainManager;

	@Inject
	FilterChainHealthCheck(DataSourceManager dataSourceManager, ProcessorChainManager processorChainManager) {
		this.dataSourceManager = dataSourceManager;
		this.processorChainManager = processorChainManager;
	}


	@Override
	protected Result check() throws Exception {
		DataSource source = dataSourceManager.findBySourceId(DATA_SOURCE_ID);
		if (processorChainManager.getAll(source).isEmpty()) return Result.unhealthy("no filter chains for source " + DATA_SOURCE_ID);
		return Result.healthy();
	}
}
