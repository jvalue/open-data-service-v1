package org.jvalue.ods.decoupleDatabase.couchdb.wrapper;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.db.DataViewRepository;
import org.jvalue.ods.decoupleDatabase.IRepository;

import java.util.List;

public class CouchDbDataViewRepositoryWrapper implements IRepository<DataView>{

	private DataViewRepository dataViewRepository;

	@Inject
	public CouchDbDataViewRepositoryWrapper(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		this.dataViewRepository = new DataViewRepository(dbConnectorFactory, databaseName);
	}


	@Override
	public DataView findById(String Id) {
		return dataViewRepository.findById(Id);
	}


	@Override
	public void add(DataView Value) {
		dataViewRepository.add(Value);
	}


	@Override
	public void update(DataView value) {
		dataViewRepository.update(value);
	}


	@Override
	public void remove(DataView Value) {
		dataViewRepository.remove(Value);
	}


	@Override
	public List<DataView> getAll() {
		return dataViewRepository.getAll();
	}
}
