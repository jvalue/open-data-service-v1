package org.jvalue.ods.monitoring;


import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;

import org.ektorp.CouchDbInstance;

import java.util.List;

public final class DbHealthCheck extends HealthCheck {

	private final CouchDbInstance dbInstance;

	@Inject
	public DbHealthCheck(CouchDbInstance dbInstance) {
		this.dbInstance = dbInstance;
	}


	@Override
	public Result check() throws Exception {
		List<String> dbs = dbInstance.getAllDatabases();
		StringBuilder builder = new StringBuilder("Found connected databases: ");
		for (String db : dbs) {
			builder.append(db);
			builder.append(" ");
		}
		return Result.healthy(builder.toString());
	}

}
