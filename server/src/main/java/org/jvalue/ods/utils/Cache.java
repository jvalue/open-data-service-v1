package org.jvalue.ods.utils;


import org.jvalue.common.utils.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Cache<T> {

	private final Map<String, T> values = new HashMap<>();

	public Cache() { }


	public void put(String key, T value) {
		Assert.assertNotNull(key, value);
		values.put(key, value);
	}


	public T remove(String key) {
		Assert.assertNotNull(key);
		return values.remove(key);
	}


	public T get(String key) {
		Assert.assertNotNull(key);
		return values.get(key);
	}


	public boolean contains(String key) {
		Assert.assertNotNull(key);
		return values.containsKey(key);
	}


	public Collection<T> getAll() {
		return values.values();
	}

}
