package org.jvalue.ods.db.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.jongo.Jongo;
import org.jvalue.commons.db.DbConnectorFactory;

public class MongoDbConnectorFactory extends DbConnectorFactory<MongoClient, Jongo> {

	public MongoDbConnectorFactory(MongoClient mongoClient, String dbPrefix) {
		super(mongoClient, dbPrefix);
	}


	@Override
	public Jongo doCreateConnector(String databaseName, boolean createIfNotExists) {
		DB db = dbInstance.getDB(dbPrefix + "-" + databaseName);
		return new Jongo(db);
	}


	@Override
	public void doDeleteDatabase(String databaseName) {
		dbInstance.dropDatabase(dbPrefix + "-" + databaseName);
	}
}
