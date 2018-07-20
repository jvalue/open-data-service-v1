package org.jvalue.ods.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.jvalue.commons.utils.Assert;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.RepositoryFactory;
import org.jvalue.ods.decoupleDatabase.IDataRepository;
import org.jvalue.commons.db.IRepository;

import java.util.List;


public final class DataViewManager extends AbstractDataSourcePropertyManager<CouchDbDataView, IRepository<CouchDbDataView>> {


	@Inject
	public DataViewManager(
			Cache<IRepository<CouchDbDataView>> viewRepositoryCache,
			RepositoryFactory repositoryFactory) {

		super(viewRepositoryCache, repositoryFactory);
	}


	public List<JsonNode> executeView(IDataRepository<CouchDbDataView, JsonNode> dataRepository, CouchDbDataView view, String argument) {
		Assert.assertNotNull(dataRepository, view);
		return dataRepository.executeQuery(view, argument);
	}


	@Override
	protected void doAdd(DataSource source, IDataRepository<CouchDbDataView, JsonNode> dataRepository, CouchDbDataView dataView) {
		dataRepository.addView(dataView);
	}


	@Override
	protected void doRemove(DataSource source, IDataRepository<CouchDbDataView, JsonNode> dataRepository, CouchDbDataView dataView) {
		dataRepository.removeView(dataView);
	}


	@Override
	protected void doRemoveAll(DataSource source) { }


	@Override
	protected IRepository<CouchDbDataView> createNewRepository(String sourceId, RepositoryFactory repositoryFactory) {
		return repositoryFactory.createDataViewRepository(sourceId);
	}

}
