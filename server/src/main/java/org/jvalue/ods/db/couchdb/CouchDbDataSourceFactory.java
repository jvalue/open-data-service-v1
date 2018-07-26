package org.jvalue.ods.db.couchdb;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.GenericRepository;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.db.DataSourceFactory;


public class CouchDbDataSourceFactory implements DataSourceFactory {

	private final DbConnectorFactory dbConnectorFactory;


	@Inject
	public CouchDbDataSourceFactory(DbConnectorFactory dbConnectorFactory){
		this.dbConnectorFactory = dbConnectorFactory;
	}
	@Override
	public GenericRepository<DataSource> createDataSource() {
		return new DataSourceRepository(dbConnectorFactory);
	}
}
