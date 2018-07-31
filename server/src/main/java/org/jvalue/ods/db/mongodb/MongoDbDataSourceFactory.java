package org.jvalue.ods.db.mongodb;

import com.google.inject.Inject;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.db.generic.DataSourceFactory;
import org.jvalue.ods.db.mongodb.repositories.MongoDbDataSourceRepository;

public class MongoDbDataSourceFactory implements DataSourceFactory {

	private final DbConnectorFactory dbConnectorFactory;


	@Inject
	public MongoDbDataSourceFactory(DbConnectorFactory dbConnectorFactory) {
		this.dbConnectorFactory = dbConnectorFactory;
	}


	@Override
	public GenericRepository<DataSource> createDataSource() {
		return new MongoDbDataSourceRepository(dbConnectorFactory);
	}
}
