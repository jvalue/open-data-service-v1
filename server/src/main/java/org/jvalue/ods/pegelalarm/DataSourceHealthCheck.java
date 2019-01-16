/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.pegelalarm;

import com.codahale.metrics.health.HealthCheck;
import org.jvalue.ods.data.DataSourceManager;

import javax.inject.Inject;

/**
 * Asserts that a {@link org.jvalue.ods.api.sources.DataSource} with the id 'pegelalarm' exists.
 */
public class DataSourceHealthCheck extends HealthCheck implements Constants {

	private final DataSourceManager dataSourceManager;

	@Inject
	DataSourceHealthCheck(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}


	@Override
	protected Result check() throws Exception {
		if (dataSourceManager.findBySourceId(DATA_SOURCE_ID) == null) return Result.unhealthy("failed to find source with id " + DATA_SOURCE_ID);
		return Result.healthy();
	}
}
