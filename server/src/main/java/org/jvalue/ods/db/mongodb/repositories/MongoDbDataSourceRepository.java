package org.jvalue.ods.db.mongodb.repositories;

import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.sources.DataSource;
import org.value.commons.mongodb.AbstractMongoDbRepository;

public class MongoDbDataSourceRepository extends AbstractMongoDbRepository<DataSource> {

	private static final String DATABASE_NAME = "dataSources";
	private static final String COLLECTION_NAME = "dataSourceCollection";


	public MongoDbDataSourceRepository(DbConnectorFactory connectorFactory) {
		super(connectorFactory, DATABASE_NAME, COLLECTION_NAME, DataSource.class);
	}


	@Override
	protected String getValueId(DataSource Value) {
		return Value.getId();
	}
}

