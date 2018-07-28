package org.jvalue.ods.db.mongodb.repositories;

import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.sources.DataSource;

public class MongoDbDataSourceRepository extends AbstractMongoDbRepository<DataSource> {

	public static final String COLLECTION_NAME = "dataSource";


	public MongoDbDataSourceRepository(DbConnectorFactory connectorFactory) {
		super(connectorFactory, COLLECTION_NAME);
	}


	@Override
	protected Class<?> getEntityType() {
		return DataSource.class;
	}
}
