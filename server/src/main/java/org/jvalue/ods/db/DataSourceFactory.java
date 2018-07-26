package org.jvalue.ods.db;

import org.jvalue.commons.db.GenericRepository;
import org.jvalue.ods.api.sources.DataSource;

public interface DataSourceFactory {
	GenericRepository<DataSource> createDataSource();
}
