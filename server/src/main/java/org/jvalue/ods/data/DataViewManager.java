package org.jvalue.ods.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.utils.Assert;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.couchdb.repositories.DataRepository;
import org.jvalue.ods.db.generic.RepositoryFactory;

import java.util.List;

/**
 * Manages the DataViewApi supported CouchDB View mechanism.
 * Only used when CouchDB is active.
 */
public final class DataViewManager extends AbstractDataSourcePropertyManager<CouchDbDataView, GenericRepository<CouchDbDataView>> {


	@Inject
	public DataViewManager(
			Cache<GenericRepository<CouchDbDataView>> viewRepositoryCache,
			RepositoryFactory repositoryFactory) {

		super(viewRepositoryCache, repositoryFactory);
	}


	public List<JsonNode> executeView(GenericDataRepository<JsonNode> dataRepository, CouchDbDataView view, String argument) {
		Assert.assertNotNull(dataRepository, view);
		DataRepository repo = (DataRepository) dataRepository;
		return repo.executeQuery(view, argument);
	}


	@Override
	protected void doAdd(DataSource source, GenericDataRepository<JsonNode> dataRepository, CouchDbDataView dataView) {
		DataRepository repo = (DataRepository) dataRepository;
		repo.addView(dataView);
	}


	@Override
	protected void doRemove(DataSource source, GenericDataRepository<JsonNode> dataRepository, CouchDbDataView dataView) {
		DataRepository repo = (DataRepository) dataRepository;
		repo.removeView(dataView);
	}


	@Override
	protected void doRemoveAll(DataSource source) { }


	@Override
	protected GenericRepository<CouchDbDataView> createNewRepository(String sourceId, RepositoryFactory repositoryFactory) {
		return repositoryFactory.createDataViewRepository(sourceId);
	}

}
