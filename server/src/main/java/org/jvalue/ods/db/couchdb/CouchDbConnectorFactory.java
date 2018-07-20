package org.jvalue.ods.db.couchdb;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.jvalue.commons.db.DbConnectorFactory;

public class CouchDbConnectorFactory extends DbConnectorFactory<CouchDbInstance, CouchDbConnector> {

	public CouchDbConnectorFactory(CouchDbInstance couchDbInstance, String dbPrefix) {
		super(couchDbInstance, dbPrefix);
	}


	@Override
	public CouchDbConnector doCreateConnector(String databaseName, boolean createIfNotExists) {
		return dbInstance.createConnector(dbPrefix + "-" + databaseName, createIfNotExists);
	}


	@Override
	public void doDeleteDatabase(String databaseName) {
		dbInstance.deleteDatabase(dbPrefix + "-" + databaseName);
	}
}
