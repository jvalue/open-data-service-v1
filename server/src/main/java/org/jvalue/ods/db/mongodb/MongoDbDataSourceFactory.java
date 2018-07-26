package org.jvalue.ods.db.mongodb;

import org.jvalue.commons.db.GenericRepository;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.db.DataSourceFactory;

public class MongoDbDataSourceFactory implements DataSourceFactory {

	@Override
	public GenericRepository<DataSource> createDataSource() {
		return null;
	}
}
