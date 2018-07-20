package org.jvalue.ods.db.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jvalue.commons.db.DbConnectorFactory;

public class MongoDbConnectorFactory extends DbConnectorFactory<MongoClient, DB > {

	public MongoDbConnectorFactory(MongoClient couchDbInstance, String dbPrefix) {
		super(couchDbInstance, dbPrefix);
	}


	@Override
	public DB doCreateConnector(String databaseName, boolean createIfNotExists) {
		return dbInstance.getDB(dbPrefix + "-" + databaseName);
	}


	@Override
	public void doDeleteDatabase(String databaseName) {
		dbInstance.dropDatabase(dbPrefix + "-" + databaseName);
	}
}
