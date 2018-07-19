package org.jvalue.ods.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.jvalue.commons.utils.Assert;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.db.RepositoryFactory;
import org.jvalue.ods.decoupleDatabase.IDataRepository;
import org.jvalue.ods.decoupleDatabase.IRepository;

import java.util.List;


public final class DataViewManager extends AbstractDataSourcePropertyManager<DataView, IRepository<DataView>> {


	@Inject
	public DataViewManager(
			Cache<IRepository<DataView>> viewRepositoryCache,
			RepositoryFactory repositoryFactory) {

		super(viewRepositoryCache, repositoryFactory);
	}


	public List<JsonNode> executeView(IDataRepository<JsonNode> dataRepository, DataView view, String argument) {
		Assert.assertNotNull(dataRepository, view);
		return dataRepository.executeQuery(view, argument);
	}


	@Override
	protected void doAdd(DataSource source, IDataRepository<JsonNode> dataRepository, DataView dataView) {
		dataRepository.addView(dataView);
	}


	@Override
	protected void doRemove(DataSource source, IDataRepository<JsonNode> dataRepository, DataView dataView) {
		dataRepository.removeView(dataView);
	}


	@Override
	protected void doRemoveAll(DataSource source) { }


	@Override
	protected IRepository<DataView> createNewRepository(String sourceId, RepositoryFactory repositoryFactory) {
		return repositoryFactory.createDataViewRepository(sourceId);
	}

}
