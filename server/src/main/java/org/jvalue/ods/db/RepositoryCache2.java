package org.jvalue.ods.db;


import org.jvalue.ods.utils.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class RepositoryCache2<T extends RepositoryAdapter<?, ?, ?>> {

	private final Map<String, T> repositories = new HashMap<>();

	public RepositoryCache2() { }


	public void put(String key, T repository) {
		Assert.assertNotNull(key, repository);
		repositories.put(key, repository);
	}


	public T remove(String key) {
		Assert.assertNotNull(key);
		return repositories.remove(key);
	}


	public T get(String key) {
		Assert.assertNotNull(key);
		return repositories.get(key);
	}


	public boolean contains(String key) {
		Assert.assertNotNull(key);
		return repositories.containsKey(key);
	}


	public Collection<T> getAll() {
		return repositories.values();
	}

}
