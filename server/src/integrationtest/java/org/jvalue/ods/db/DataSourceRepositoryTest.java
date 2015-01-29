package org.jvalue.ods.db;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.ektorp.CouchDbInstance;
import org.jvalue.common.db.RepositoryAdapter;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;

public class DataSourceRepositoryTest extends AbstractRepositoryAdapterTest<DataSource> {

	public DataSourceRepositoryTest() {
		super(DataSourceRepositoryTest.class.getSimpleName());
	}


	@Override
	protected RepositoryAdapter<?, ?, DataSource> doCreateAdapter(CouchDbInstance couchDbInstance, String databaseName) {
		return new DataSourceRepository(couchDbInstance.createConnector(databaseName, true));
	}


	@Override
	protected DataSource doCreateValue(String id, String data) {
		return new DataSource(id,
				JsonPointer.compile("/" + data),
				new ObjectNode(JsonNodeFactory.instance),
				new DataSourceMetaData("", "", "", "", "", "", ""));
	}

}
