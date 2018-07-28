package org.jvalue.ods.db.couchdb;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.commons.couchdb.test.AbstractRepositoryAdapterTest;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;
import org.jvalue.ods.db.couchdb.repositories.DataSourceRepository;

public class DataSourceRepositoryTest extends AbstractRepositoryAdapterTest<DataSource> {

	@Override
	protected RepositoryAdapter<?, ?, DataSource> doCreateAdapter(DbConnectorFactory connectorFactory) {
		return new DataSourceRepository((DbConnectorFactory) connectorFactory.createConnector(getClass().getSimpleName(), true));
	}


	@Override
	protected DataSource doCreateValue(String id, String data) {
		return new DataSource(id,
				JsonPointer.compile("/" + data),
				new ObjectNode(JsonNodeFactory.instance),
				new DataSourceMetaData("", "", "", "", "", "", ""));
	}

}
