package org.jvalue.ods.admin.monitoring;


import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;

import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.db.DataSourceRepository;
import org.jvalue.ods.decoupleDatabase.IRepository;
import org.jvalue.ods.decoupleDatabase.couchdb.wrapper.CouchDbDataSourceRepositoryWrapper;

/**
 * Checks that CouchDb is reachable.
 */
public final class DbHealthCheck extends HealthCheck {

	private final IRepository<DataSource> sourceRepository;

	@Inject
	public DbHealthCheck(IRepository<DataSource> sourceRepository) {
		this.sourceRepository = sourceRepository;
	}


	@Override
	public Result check() throws Exception {
		sourceRepository.getAll();
		return Result.healthy();
	}

}
