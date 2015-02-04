package org.jvalue.ods.db;


import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

/**
 * Running integration tests against CouchDB requires a admin account to be setup on that
 * CouchDB instance with "admin" "admin" credentials. This class provides easier access to
 * creating connector instances.
 */
public final class DbFactory {

	private DbFactory() { }

	public static HttpClient createHttpClient() {
		return new StdHttpClient.Builder()
				.username("admin")
				.password("admin")
				.build();
	}


	public static CouchDbInstance createCouchDbInstance() {
		return new StdCouchDbInstance(createHttpClient());
	}

}
