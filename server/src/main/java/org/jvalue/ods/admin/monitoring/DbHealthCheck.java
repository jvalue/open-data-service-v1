package org.jvalue.ods.admin.monitoring;


import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.db.DataSourceFactory;

/**
 * Checks that CouchDb is reachable.
 */
public final class DbHealthCheck extends HealthCheck {

	private final GenericRepository<DataSource> sourceRepository;

	@Inject
	public DbHealthCheck(DataSourceFactory dataSourceFactory) {
		this.sourceRepository = dataSourceFactory.createDataSource();
	}


	@Override
	public Result check() throws Exception {
		sourceRepository.getAll();
		return Result.healthy();
	}

}
