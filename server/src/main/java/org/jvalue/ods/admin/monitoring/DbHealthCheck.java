/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.admin.monitoring;


import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import org.jvalue.ods.db.DataSourceRepository;

/**
 * Checks that CouchDb is reachable.
 */
public final class DbHealthCheck extends HealthCheck {

	private final DataSourceRepository sourceRepository;

	@Inject
	public DbHealthCheck(DataSourceRepository sourceRepository) {
		this.sourceRepository = sourceRepository;
	}


	@Override
	public Result check() throws Exception {
		sourceRepository.getAll();
		return Result.healthy();
	}

}
