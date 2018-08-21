package org.jvalue.ods.db.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.jvalue.commons.db.DbConnectorFactory;

public class MongoDbConnectorFactory extends DbConnectorFactory<MongoClient, MongoDatabase> {

	public MongoDbConnectorFactory(MongoClient mongoClient, String dbPrefix) {
		super(mongoClient, dbPrefix);
	}


	@Override
	public MongoDatabase doCreateConnector(String databaseName, boolean createIfNotExists) {
		return dbInstance.getDatabase(dbPrefix + "-" + databaseName);
	}


	@Override
	public void doDeleteDatabase(String databaseName) {
		dbInstance.dropDatabase(dbPrefix + "-" + databaseName);
	}
}
