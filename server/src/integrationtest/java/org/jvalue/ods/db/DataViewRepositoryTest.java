package org.jvalue.ods.db;


import org.jvalue.common.db.DbConnectorFactory;
import org.jvalue.common.db.RepositoryAdapter;
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
