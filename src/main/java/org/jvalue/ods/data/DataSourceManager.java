package org.jvalue.ods.data;


import com.google.inject.Inject;

import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.RepositoryCache;
import org.jvalue.ods.db.DataSourceRepository;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.filter.FilterChainManager;
import org.jvalue.ods.filter.reference.FilterChainReference;
import org.jvalue.ods.utils.Assert;

import java.util.List;

import io.dropwizard.lifecycle.Managed;

public final class DataSourceManager implements Managed {

	private final DataSourceRepository dataSourceRepository;
	private final RepositoryCache<DataRepository> dataRepositoryCache;
	private final CouchDbInstance dbInstance;
	private final DbFactory dbFactory;
	private final FilterChainManager filterChainManager;

	@Inject
	public DataSourceManager(
			DataSourceRepository dataSourceRepository,
			RepositoryCache<DataRepository> dataRepositoryCache,
			CouchDbInstance dbInstance,
			DbFactory dbFactory,
			FilterChainManager filterChainManager) {

		this.dataSourceRepository = dataSourceRepository;
		this.dataRepositoryCache = dataRepositoryCache;
		this.dbInstance = dbInstance;
		this.dbFactory = dbFactory;
		this.filterChainManager = filterChainManager;
	}


	public void add(DataSource source) {
		Assert.assertNotNull(source);

		// create data repository
		createDataRepository(source);

		// store source
		dataSourceRepository.add(source);
	}


	public void remove(DataSource source) {
		Assert.assertNotNull(source);

		// delete source
		dataSourceRepository.remove(source);

		// delete data db
		dataRepositoryCache.remove(source.getSourceId());
		dbInstance.deleteDatabase(source.getSourceId());

		// delete filter chains
		for (FilterChainReference reference : filterChainManager.getAllForSource(source.getSourceId())) {
			filterChainManager.remove(reference);
		}
	}


	public List<DataSource> getAll() {
		return dataSourceRepository.getAll();
	}


	public DataSource findBySourceId(String sourceId) throws DocumentNotFoundException {
		return dataSourceRepository.findBySourceId(sourceId);
	}


	public DataRepository getDataRepository(String sourceId) {
		Assert.assertNotNull(sourceId);
		return dataRepositoryCache.getForKey(sourceId);
	}


	public boolean isValidSourceId(String sourceId) {
		try {
			findBySourceId(sourceId);
			return true;
		} catch (DocumentNotFoundException dnfe) {
			return false;
		}
	}


	@Override
	public void start() {
		for (DataSource source : dataSourceRepository.getAll()) { createDataRepository(source);
		}
	}


	@Override
	public void stop() {
		// nothing to do here
	}


	private void createDataRepository(DataSource source) {
		DataRepository dataRepository = dbFactory.createSourceDataRepository(source.getSourceId(), source.getDomainIdKey());
		dataRepositoryCache.put(source.getSourceId(), dataRepository);
	}

}
