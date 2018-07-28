package org.jvalue.ods.db.generic;

import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.api.sources.DataSource;

public interface DataSourceFactory {
	GenericRepository<DataSource> createDataSource();
}
