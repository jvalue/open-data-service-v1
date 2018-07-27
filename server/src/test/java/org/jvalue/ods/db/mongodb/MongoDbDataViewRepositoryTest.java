package org.jvalue.ods.db.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.junit.Before;
import org.junit.Test;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.mongodb.repositories.MongoDbDataViewRepository;

public class MongoDbDataViewRepositoryTest {


	MongoDbDataViewRepository dataViewRepository;
	@Before
	public void setUp(){

		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://127.0.0.1:27017"));
		String dbPrefix = "test";
		DbConnectorFactory connectorFactory = new MongoDbConnectorFactory(mongoClient, dbPrefix);
		dataViewRepository = new MongoDbDataViewRepository(connectorFactory, "dataViews");
	}

	@Test
	public void testSaveDataSource(){
		CouchDbDataView dataView = new CouchDbDataView("uniqueId", "function(lol)", "function(lol2)");
		dataViewRepository.add(dataView);
	}
}
