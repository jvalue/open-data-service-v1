package org.jvalue.ods.db;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class DbModule extends AbstractModule {

	private final static String DATABASE_NAME_CLIENTS = "notificationClients";

	@Override
	protected void configure() {
		// TODO this not not yet perfect, as it does not get a CouchDbInstance injected
		bind(new TypeLiteral<DbAccessor<JsonNode>>() { }).toInstance(new CouchDbAccessor("ods"));
	}


	@Provides
	@Singleton
	protected CouchDbInstance provideCouchDbInstance() {
		HttpClient httpClient = new StdHttpClient.Builder().build();
		return new StdCouchDbInstance(httpClient);
	}


	@Provides
	@Singleton
	@NotificationsDb
	@Inject
	protected CouchDbConnector provideNotificationsDbConnector(CouchDbInstance couchDbInstance) {
		return couchDbInstance.createConnector(DATABASE_NAME_CLIENTS, true);
	}

}
