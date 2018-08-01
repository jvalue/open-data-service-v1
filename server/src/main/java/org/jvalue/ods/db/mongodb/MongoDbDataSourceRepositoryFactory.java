package org.jvalue.ods.db.mongodb;

import com.google.inject.Inject;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.db.generic.DataSourceRepositoryFactory;
import org.jvalue.ods.db.mongodb.repositories.MongoDbDataSourceRepository;

public class MongoDbDataSourceRepositoryFactory implements DataSourceRepositoryFactory {

	private final DbConnectorFactory dbConnectorFactory;


	@Inject
	public MongoDbDataSourceRepositoryFactory(DbConnectorFactory dbConnectorFactory) {
		this.dbConnectorFactory = dbConnectorFactory;
	}


	@Override
	public GenericRepository<DataSource> createDataSource() {
		return new MongoDbDataSourceRepository(dbConnectorFactory);
	}
}
