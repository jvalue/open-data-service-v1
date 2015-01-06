package org.jvalue.ods.db;


import org.ektorp.CouchDbInstance;
import org.jvalue.ods.api.views.DataView;

public class DataViewRepositoryTest extends AbstractRepositoryAdapterTest<DataView> {

	public DataViewRepositoryTest() {
		super(DataViewRepositoryTest.class.getSimpleName());
	}


	@Override
	protected RepositoryAdapter<?, ?, DataView> doCreateAdapter(CouchDbInstance couchDbInstance, String databaseName) {
		return new DataViewRepository(couchDbInstance, databaseName);
	}


	@Override
	protected DataView doCreateValue(String id, String data) {
		return new DataView(id, data);
	}

}
