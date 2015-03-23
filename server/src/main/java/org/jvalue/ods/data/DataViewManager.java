package org.jvalue.ods.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import org.jvalue.commons.utils.Assert;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.DataViewRepository;
import org.jvalue.ods.db.RepositoryFactory;
import org.jvalue.ods.utils.Cache;

import java.util.List;


public final class DataViewManager extends AbstractDataSourcePropertyManager<DataView, DataViewRepository> {


	@Inject
	DataViewManager(
			Cache<DataViewRepository> viewRepositoryCache,
			RepositoryFactory repositoryFactory) {

		super(viewRepositoryCache, repositoryFactory);
	}


	public List<JsonNode> executeView(DataRepository dataRepository, DataView view, String argument) {
		Assert.assertNotNull(dataRepository, view);
		return dataRepository.executeQuery(view, argument);
	}


	@Override
	protected void doAdd(DataSource source, DataRepository dataRepository, DataView dataView) {
		dataRepository.addView(dataView);
	}


	@Override
	protected void doRemove(DataSource source, DataRepository dataRepository, DataView dataView) {
		dataRepository.removeView(dataView);
	}


	@Override
	protected void doRemoveAll(DataSource source) { }


	@Override
	protected DataViewRepository createNewRepository(String sourceId, RepositoryFactory repositoryFactory) {
		return repositoryFactory.createDataViewRepository(sourceId);
	}

}
