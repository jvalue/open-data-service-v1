package org.jvalue.ods.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.jvalue.commons.db.GenericDataRepository;
import org.jvalue.commons.db.GenericRepository;
import org.jvalue.commons.utils.Assert;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.couchdb.RepositoryFactory;

import java.util.List;


public final class DataViewManager extends AbstractDataSourcePropertyManager<CouchDbDataView, GenericRepository<CouchDbDataView>> {


	@Inject
	public DataViewManager(
			Cache<GenericRepository<CouchDbDataView>> viewRepositoryCache,
			RepositoryFactory repositoryFactory) {

		super(viewRepositoryCache, repositoryFactory);
	}


	public List<JsonNode> executeView(GenericDataRepository<CouchDbDataView, JsonNode> dataRepository, CouchDbDataView view, String argument) {
		Assert.assertNotNull(dataRepository, view);
		return dataRepository.executeQuery(view, argument);
	}


	@Override
	protected void doAdd(DataSource source, GenericDataRepository<CouchDbDataView, JsonNode> dataRepository, CouchDbDataView dataView) {
		dataRepository.addQuery(dataView);
	}


	@Override
	protected void doRemove(DataSource source, GenericDataRepository<CouchDbDataView, JsonNode> dataRepository, CouchDbDataView dataView) {
		dataRepository.removeQuery(dataView);
	}


	@Override
	protected void doRemoveAll(DataSource source) { }


	@Override
	protected GenericRepository<CouchDbDataView> createNewRepository(String sourceId, RepositoryFactory repositoryFactory) {
		return repositoryFactory.createDataViewRepository(sourceId);
	}

}
