package org.jvalue.ods.pegelalarm;

import com.codahale.metrics.health.HealthCheck;

import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.db.couchdb.data.DataSourceManager;
import org.jvalue.ods.notifications.NotificationManager;

import java.util.List;

import javax.inject.Inject;

/**
 * Asserts that the CEPS is registered as a client for the pegel alarm data source.
 */
public class CepsClientHealthCheck extends HealthCheck implements Constants {

	private final NotificationManager notificationManager;
	private final DataSourceManager dataSourceManager;

	@Inject
	CepsClientHealthCheck(NotificationManager notificationManager, DataSourceManager dataSourceManager) {
		this.notificationManager = notificationManager;
		this.dataSourceManager = dataSourceManager;
	}

	@Override
	protected Result check() throws Exception {
		final DataSource source = dataSourceManager.findBySourceId(DATA_SOURCE_ID);
		List<Client> clients = notificationManager.getAll(source);

		Client cepsClient = null;
		for (Client client : clients) {
			if (client.getId().equals(CEPS_CLIENT_ID)) {
				cepsClient = client;
				break;
			}
		}
		if (cepsClient == null) return Result.unhealthy("CEPS is not registered as a client for receiving data updates");
		return Result.healthy();
	}

}
