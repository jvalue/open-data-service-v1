package org.jvalue.ods.data;


import com.google.inject.Inject;

import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.DataRepositoryCache;
import org.jvalue.ods.db.DataSourceRepository;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.FilterChainReferenceRepository;
import org.jvalue.ods.filter.reference.FilterChainReference;
import org.jvalue.ods.utils.Assert;

import java.util.List;

public final class DataSourceManager {

	private final DataSourceRepository dataSourceRepository;
	private final DataRepositoryCache dataRepositoryCache;
	private final CouchDbInstance dbInstance;
	private final DbFactory dbFactory;
	private final FilterChainReferenceRepository filterChainReferenceRepository;

	@Inject
	public DataSourceManager(
			DataSourceRepository dataSourceRepository,
			DataRepositoryCache dataRepositoryCache,
			CouchDbInstance dbInstance,
			DbFactory dbFactory,
			FilterChainReferenceRepository filterChainReferenceRepository) {

		this.dataSourceRepository = dataSourceRepository;
		this.dataRepositoryCache = dataRepositoryCache;
		this.dbInstance = dbInstance;
		this.dbFactory = dbFactory;
		this.filterChainReferenceRepository = filterChainReferenceRepository;
	}


	public void add(DataSource source) {
		Assert.assertNotNull(source);

		// create data repository
		DataRepository dataRepository = dbFactory.createSourceDataRepository(source.getSourceId(), source.getDomainIdKey());
		dataRepositoryCache.putRepository(source.getSourceId(), dataRepository);

		// store source
		dataSourceRepository.add(source);
	}


	public void remove(DataSource source) {
		Assert.assertNotNull(source);

		// delete source
		dataSourceRepository.remove(source);

		// delete data db
		dataRepositoryCache.removeRepository(source.getSourceId());
		dbInstance.deleteDatabase(source.getSourceId());

		// delete filter chains
		for (FilterChainReference reference : filterChainReferenceRepository.findByDataSourceId(source.getSourceId())) {
			filterChainReferenceRepository.remove(reference);
		}
	}


	public List<DataSource> getAll() {
		return dataSourceRepository.getAll();
	}


	public DataSource findBySourceId(String sourceId) throws DocumentNotFoundException {
		return dataSourceRepository.findBySourceId(sourceId);
	}


	public boolean isValidSourceId(String sourceId) {
		try {
			findBySourceId(sourceId);
			return true;
		} catch (DocumentNotFoundException dnfe) {
			return false;
		}
	}

}
