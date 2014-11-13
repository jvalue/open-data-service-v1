package org.jvalue.ods.db;


import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class DbModule extends AbstractModule {

	private final static String DATABASE_NAME_CLIENTS = "notificationClients";

	@Override
	protected void configure() {
		CouchDbInstance couchDbInstance = new StdCouchDbInstance(new StdHttpClient.Builder().build());
		CouchDbConnector notificationsConnector = couchDbInstance.createConnector(DATABASE_NAME_CLIENTS, true);

		bind(CouchDbInstance.class).toInstance(couchDbInstance);
		bind(CouchDbConnector.class).annotatedWith(NotificationsDb.class).toInstance(notificationsConnector);
		install(new FactoryModuleBuilder()
				.implement(SourceDataRepository.class, SourceDataRepository.class)
				.build(DbFactory.class));
	}

}
