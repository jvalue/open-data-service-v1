package org.jvalue.ods.db;


import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.commons.couchdb.test.AbstractRepositoryAdapterTest;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.couchdb.repositories.DataViewRepository;

public class DataViewRepositoryTest extends AbstractRepositoryAdapterTest<CouchDbDataView> {

	@Override
	protected RepositoryAdapter<?, ?, CouchDbDataView> doCreateAdapter(DbConnectorFactory connectorFactory) {
		return new DataViewRepository(connectorFactory, getClass().getSimpleName());
	}


	@Override
	protected CouchDbDataView doCreateValue(String id, String data) {
		return new CouchDbDataView(id, data);
	}

}
