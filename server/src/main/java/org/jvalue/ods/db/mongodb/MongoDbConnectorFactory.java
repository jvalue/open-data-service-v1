package org.jvalue.ods.db.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.jvalue.commons.db.DbConnectorFactory;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDbConnectorFactory extends DbConnectorFactory<MongoClient, MongoDatabase > {

	public MongoDbConnectorFactory(MongoClient mongoClient, String dbPrefix) {
		super(mongoClient, dbPrefix);
	}


	@Override
	public MongoDatabase doCreateConnector(String databaseName, boolean createIfNotExists) {
		// create codec registry for POJOs
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
			fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		return dbInstance.getDatabase(dbPrefix + "-" + databaseName).withCodecRegistry(pojoCodecRegistry);
	}

	@Override
	public void doDeleteDatabase(String databaseName) {
		dbInstance.dropDatabase(dbPrefix + "-" + databaseName);
	}
}
