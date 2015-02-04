package org.jvalue.common.db;


import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;

/**
 * Responsible for creating {@link org.ektorp.CouchDbConnector} instances and prefixing the DB names.
 */
public final class DbConnectorFactory {

	private final CouchDbInstance couchDbInstance;
	private final String dbPrefix;

	public DbConnectorFactory(CouchDbInstance couchDbInstance, String dbPrefix) {
		this.couchDbInstance = couchDbInstance;
		this.dbPrefix = dbPrefix;
	}


	public CouchDbConnector createConnector(String databaseName, boolean createIfNotExists) {
		return couchDbInstance.createConnector(dbPrefix + "-" + databaseName, createIfNotExists);
	}


	public void deleteDatabase(String databaseName) {
		couchDbInstance.deleteDatabase(dbPrefix + "-" + databaseName);
	}

}
