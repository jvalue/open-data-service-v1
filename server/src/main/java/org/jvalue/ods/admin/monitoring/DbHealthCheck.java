package org.jvalue.ods.monitoring;


import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;

import org.ektorp.CouchDbInstance;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.DataSourceRepository;

import java.util.List;

import javax.xml.crypto.Data;

/**
 * Checks that CouchDb is still reachable.
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
		return Result.healthy(DataSourceRepository.class.getSimpleName() + " is reachable");
	}

}
