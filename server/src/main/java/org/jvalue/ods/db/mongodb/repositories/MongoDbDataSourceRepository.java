package org.jvalue.ods.db.mongodb.repositories;

import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.sources.DataSource;

public class MongoDbDataSourceRepository extends AbstractMongoDbRepository<DataSource> {

	public static final String DATABASE_NAME = "dataSources";
	public static final String COLLECTION_NAME = "dataSourceCollection";


	public MongoDbDataSourceRepository(DbConnectorFactory connectorFactory) {
		super(connectorFactory, DATABASE_NAME, COLLECTION_NAME);
	}


	@Override
	protected Class<?> getEntityType() {
		return DataSource.class;
	}
}

