package org.jvalue.ods.db.mongodb;


import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.mongodb.test.AbstractRepositoryTestBase;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.mongodb.repositories.MongoDbDataViewRepository;

public class MongoDbDataViewRepositoryTest extends AbstractRepositoryTestBase<CouchDbDataView> {

	@Override
	protected GenericRepository<CouchDbDataView> doCreateRepository(DbConnectorFactory connectorFactory) {
		return new MongoDbDataViewRepository(connectorFactory, getClass().getSimpleName());
	}


	@Override
	protected CouchDbDataView doCreateValue(String id, String data) {
		return new CouchDbDataView(id, data);
	}


	@Override
	protected void doDeleteDatabase(DbConnectorFactory dbConnectorFactory) {
		dbConnectorFactory.doDeleteDatabase(getClass().getSimpleName());
	}
}
