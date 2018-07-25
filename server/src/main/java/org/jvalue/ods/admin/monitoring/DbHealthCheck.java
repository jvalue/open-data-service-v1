package org.jvalue.ods.admin.monitoring;


import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.commons.db.GenericRepository;

/**
 * Checks that CouchDb is reachable.
 */
public final class DbHealthCheck extends HealthCheck {

	private final GenericRepository<DataSource> sourceRepository;

	@Inject
	public DbHealthCheck(GenericRepository<DataSource> sourceRepository) {
		this.sourceRepository = sourceRepository;
	}


	@Override
	public Result check() throws Exception {
		sourceRepository.getAll();
		return Result.healthy();
	}

}
