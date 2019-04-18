package org.jvalue.ods;


import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import java.util.Optional;

/**
 * Running integration tests against CouchDB requires a admin account to be setup on that
 * CouchDB instance with "admin" "admin" credentials. This class provides easier access to
 * creating connector instances.
 */
public final class DbFactory {

	private static final String COUCHDB_HOST = Optional.ofNullable(System.getenv("ODS_IT_COUCHDB_HOST")).orElse("localhost");

	private DbFactory() { }

	public static HttpClient createHttpClient() {


		return new StdHttpClient.Builder()
				.username("admin")
				.password("admin")
				.host(COUCHDB_HOST)
				.build();
	}


	public static CouchDbInstance createCouchDbInstance() {
		return new StdCouchDbInstance(createHttpClient());
	}

}
