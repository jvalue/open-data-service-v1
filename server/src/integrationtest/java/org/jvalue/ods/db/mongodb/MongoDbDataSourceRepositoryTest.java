package org.jvalue.ods.db.mongodb;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.mongodb.test.AbstractRepositoryAdapterTest;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;
import org.jvalue.ods.db.mongodb.repositories.MongoDbDataSourceRepository;

@Ignore
public class MongoDbDataSourceRepositoryTest extends AbstractRepositoryAdapterTest<DataSource> {
	@Override
	protected GenericRepository<DataSource> doCreateRepository(DbConnectorFactory connectorFactory) {
		return new MongoDbDataSourceRepository(connectorFactory);
	}


	@Override
	protected DataSource doCreateValue(String id, String data) {
		return new DataSource(id,
			JsonPointer.compile("/" + data),
			new ObjectNode(JsonNodeFactory.instance),
			new DataSourceMetaData("", "", "", "", "", "", ""));
	}


	@Override
	protected void doDeleteDatabase(DbConnectorFactory dbConnectorFactory) {
		dbConnectorFactory.doDeleteDatabase("dataSources");
	}
}
