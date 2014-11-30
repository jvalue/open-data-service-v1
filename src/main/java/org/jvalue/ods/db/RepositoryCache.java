package org.jvalue.ods.db;


import org.ektorp.support.CouchDbRepositorySupport;
import org.jvalue.ods.utils.Assert;

import java.util.HashMap;
import java.util.Map;

public final class RepositoryCache<T extends CouchDbRepositorySupport<?>> {

	private final Map<String, T> repositories = new HashMap<>();

	public RepositoryCache() { }


	public void put(String key, T repository) {
		Assert.assertNotNull(key, repository);
		repositories.put(key, repository);
	}


	public T remove(String key) {
		Assert.assertNotNull(key);
		return repositories.remove(key);
	}


	public T getForKey(String key) {
		Assert.assertNotNull(key);
		return repositories.get(key);
	}

}
