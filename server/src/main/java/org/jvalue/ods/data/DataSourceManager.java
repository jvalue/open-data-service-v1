package org.jvalue.ods.data;


import com.google.inject.Inject;

import org.ektorp.DocumentNotFoundException;
import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.commons.utils.Assert;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.DataSourceRepository;
import org.jvalue.ods.db.RepositoryFactory;
import org.jvalue.ods.notifications.NotificationManager;
import org.jvalue.ods.processor.ProcessorChainManager;
import org.jvalue.ods.utils.Cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dropwizard.lifecycle.Managed;

public final class DataSourceManager implements Managed {

	private final DataSourceRepository dataSourceRepository;
	private final Cache<DataRepository> dataRepositoryCache;
	private final DbConnectorFactory dbConnectorFactory;
	private final RepositoryFactory repositoryFactory;
	private final ProcessorChainManager processorChainManager;
	private final DataViewManager dataViewManager;
	private final NotificationManager notificationManager;

	@Inject
	public DataSourceManager(
			DataSourceRepository dataSourceRepository,
			Cache<DataRepository> dataRepositoryCache,
			DbConnectorFactory dbConnectorFactory,
			RepositoryFactory repositoryFactory,
			ProcessorChainManager processorChainManager,
			DataViewManager dataViewManager,
			NotificationManager notificationManager) {

		this.dataSourceRepository = dataSourceRepository;
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


	public DataSource findBySourceId(String sourceId) throws DocumentNotFoundException {
		return dataSourceRepository.findById(sourceId);
	}


	public DataRepository getDataRepository(DataSource source) {
		Assert.assertNotNull(source);
		return dataRepositoryCache.get(source.getId());
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
		// create data repositories
		Map<DataSource, DataRepository> sources = new HashMap<>();
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


	private DataRepository createDataRepository(DataSource source) {
		DataRepository dataRepository = repositoryFactory.createSourceDataRepository(source.getId(), source.getDomainIdKey());
		dataRepositoryCache.put(source.getId(), dataRepository);
		return dataRepository;
	}

}
