package org.jvalue.ods.db.mongodb;


import jdk.nashorn.internal.ir.annotations.Ignore;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.mongodb.test.AbstractRepositoryAdapterTest;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.mongodb.repositories.MongoDbDataViewRepository;

@Ignore
public class MongoDbDataViewRepositoryTest extends AbstractRepositoryAdapterTest<CouchDbDataView> {

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
