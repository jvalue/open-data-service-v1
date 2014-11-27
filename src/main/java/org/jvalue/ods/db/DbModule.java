package org.jvalue.ods.db;


import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class DbModule extends AbstractModule {

	@Override
	protected void configure() {
		CouchDbInstance couchDbInstance = new StdCouchDbInstance(new StdHttpClient.Builder().build());
		CouchDbConnector notificationsConnector = couchDbInstance.createConnector(NotificationClientRepository.DATABASE_NAME, true);

		bind(CouchDbInstance.class).toInstance(couchDbInstance);
		bind(CouchDbConnector.class).annotatedWith(Names.named(NotificationClientRepository.DATABASE_NAME)).toInstance(notificationsConnector);
		install(new FactoryModuleBuilder()
				.implement(DataRepository.class, DataRepository.class)
				.build(DbFactory.class));
	}

}
