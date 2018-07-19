package org.jvalue.ods.decoupleDatabase.couchdb.wrapper;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import org.ektorp.CouchDbConnector;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.db.DataSourceRepository;
import org.jvalue.ods.decoupleDatabase.IRepository;

import java.util.List;

public class CouchDbDataSourceRepositoryWrapper implements IRepository<DataSource>{

	static final String DATABASE_NAME = "dataSources";
	private DataSourceRepository dataSourceRepository;

	@Inject
	public CouchDbDataSourceRepositoryWrapper(@Named(DATABASE_NAME)  CouchDbConnector couchDbConnector) {
		this.dataSourceRepository = new DataSourceRepository(couchDbConnector);
	}


	@Override
	public DataSource findById(String Id) {
		return dataSourceRepository.findById(Id);
	}


	@Override
	public void add(DataSource Value) {
		dataSourceRepository.add(Value);
	}


	@Override
	public void update(DataSource value) {
		dataSourceRepository.update(value);
	}


	@Override
	public void remove(DataSource Value) {
		dataSourceRepository.remove(Value);
	}


	@Override
	public List<DataSource> getAll() {
		return dataSourceRepository.getAll();
	}
}
