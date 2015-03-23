package org.jvalue.ods.db;


import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.ods.api.views.DataView;

public class DataViewRepositoryTest extends AbstractRepositoryAdapterTest<DataView> {

	@Override
	protected RepositoryAdapter<?, ?, DataView> doCreateAdapter(DbConnectorFactory connectorFactory) {
		return new DataViewRepository(connectorFactory, getClass().getSimpleName());
	}


	@Override
	protected DataView doCreateValue(String id, String data) {
		return new DataView(id, data);
	}

}
