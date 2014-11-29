package org.jvalue.ods.db;


import org.jvalue.ods.utils.Assert;

import java.util.HashMap;
import java.util.Map;

public final class DataRepositoryCache {

	private final Map<String, DataRepository> repositories = new HashMap<>();


	public DataRepositoryCache() { }


	public void putRepository(String dataSourceId, DataRepository repository) {
		Assert.assertNotNull(dataSourceId, repository);
		repositories.put(dataSourceId, repository);
	}


	public DataRepository removeRepository(String dataSourceId) {
		Assert.assertNotNull(dataSourceId);
		return repositories.remove(dataSourceId);
	}


	public DataRepository getRepositoryForSourceId(String dataSourceId) {
		return repositories.get(dataSourceId);
	}

}
