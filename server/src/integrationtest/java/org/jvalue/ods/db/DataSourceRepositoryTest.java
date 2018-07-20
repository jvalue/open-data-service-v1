package org.jvalue.ods.db;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.commons.couchdb.test.AbstractRepositoryAdapterTest;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;
import org.jvalue.ods.db.couchdb.DataSourceRepository;

public class DataSourceRepositoryTest extends AbstractRepositoryAdapterTest<DataSource> {

	@Override
	protected RepositoryAdapter<?, ?, DataSource> doCreateAdapter(DbConnectorFactory connectorFactory) {
		return new DataSourceRepository(connectorFactory.createConnector(getClass().getSimpleName(), true));
	}


	@Override
	protected DataSource doCreateValue(String id, String data) {
		return new DataSource(id,
				JsonPointer.compile("/" + data),
				new ObjectNode(JsonNodeFactory.instance),
				new DataSourceMetaData("", "", "", "", "", "", ""));
	}

}
