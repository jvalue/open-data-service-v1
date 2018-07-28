package org.jvalue.ods.db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.utils.Assert;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.mongodb.MongoDbConnectorFactory;
import org.jvalue.ods.db.mongodb.repositories.MongoDbDataViewRepository;

@Ignore // ignore as there is no mongodb environment on build server
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
		CouchDbDataView dataView = new CouchDbDataView("507f1f77bcf86cd799439011", "function(lol)", "function(lol2)");
		CouchDbDataView byId = dataViewRepository.findById(dataView.getId());
		if(byId != null){
			dataViewRepository.remove(dataView);
		}

		dataViewRepository.add(dataView);

		Assert.assertNotNull(dataViewRepository.findById(dataView.getId()));

		dataViewRepository.remove(dataView);

		Assert.assertTrue(dataViewRepository.findById(dataView.getId()) == null);

	}
}
