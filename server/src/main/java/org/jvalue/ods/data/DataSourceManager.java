package org.jvalue.ods.data;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import io.dropwizard.lifecycle.Managed;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.GenericDocumentNotFoundException;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.utils.Assert;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.generic.DataSourceFactory;
import org.jvalue.ods.db.generic.RepositoryFactory;
import org.jvalue.ods.notifications.NotificationManager;
import org.jvalue.ods.processor.ProcessorChainManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataSourceManager implements Managed {

	private final GenericRepository<DataSource> dataSourceRepository;
	private final Cache<GenericDataRepository<CouchDbDataView, JsonNode>> dataRepositoryCache;
	private final DbConnectorFactory dbConnectorFactory;
	private final RepositoryFactory repositoryFactory;
	private final ProcessorChainManager processorChainManager;
	private final DataViewManager dataViewManager;
	private final NotificationManager notificationManager;

	@Inject
	public DataSourceManager(
		DataSourceFactory dataSourceFactory,
		Cache<GenericDataRepository<CouchDbDataView, JsonNode>> dataRepositoryCache,
		DbConnectorFactory dbConnectorFactory,
		RepositoryFactory repositoryFactory,
		ProcessorChainManager processorChainManager,
		DataViewManager dataViewManager,
		NotificationManager notificationManager) {

		this.dataSourceRepository = dataSourceFactory.createDataSource();
		this.dataRepositoryCache = dataRepositoryCache;
		this.dbConnectorFactory = dbConnectorFactory;
		this.repositoryFactory = repositoryFactory;
		this.processorChainManager = processorChainManager;
		this.dataViewManager = dataViewManager;
		this.notificationManager = notificationManager;
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

		// delete source db
		dataRepositoryCache.remove(source.getId());
		processorChainManager.removeAll(source);
		dataViewManager.removeAll(source);
		notificationManager.removeAll(source);
		dbConnectorFactory.deleteDatabase(source.getId());
	}


	public List<DataSource> getAll() {
		return dataSourceRepository.getAll();
	}


	public DataSource findBySourceId(String sourceId) throws GenericDocumentNotFoundException {
		return dataSourceRepository.findById(sourceId);
	}


	public GenericDataRepository<CouchDbDataView, JsonNode> getDataRepository(DataSource source) {
		Assert.assertNotNull(source);
		return dataRepositoryCache.get(source.getId());
	}


	public boolean isValidSourceId(String sourceId) {
		try {
			findBySourceId(sourceId);
			return true;
		} catch (GenericDocumentNotFoundException dnfe) {
			return false;
		}
	}


	@Override
	public void start() {
		// create data repositories
		Map<DataSource, GenericDataRepository<CouchDbDataView, JsonNode>> sources = new HashMap<>();
		for (DataSource source : dataSourceRepository.getAll()) {
			sources.put(source, createDataRepository(source));
		}

		// start filter chains
		processorChainManager.startAllProcessorChains(sources);
	}


	@Override
	public void stop() {
		processorChainManager.stopAllProcessorChains();
	}


	private GenericDataRepository<CouchDbDataView, JsonNode> createDataRepository(DataSource source) {
		GenericDataRepository<CouchDbDataView, JsonNode> dataRepository = repositoryFactory.createSourceDataRepository(source.getId(), source.getDomainIdKey());
		dataRepositoryCache.put(source.getId(), dataRepository);
		return dataRepository;
	}

}
