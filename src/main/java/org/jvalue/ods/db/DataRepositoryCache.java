package org.jvalue.ods.db;


import org.jvalue.ods.utils.Assert;

import java.util.HashMap;
import java.util.Map;

public final class DataRepositoryCache {

	private final Map<String, DataRepository> repositories = new HashMap<String, DataRepository>();


	public DataRepositoryCache() { }


	public void putRepository(String dataSourceId, DataRepository repository) {
		Assert.assertNotNull(dataSourceId, repository);
		repositories.put(dataSourceId, repository);
	}


	public DataRepository getRepositoryForSourceId(String dataSourceId) {
		return repositories.get(dataSourceId);
	}

}
