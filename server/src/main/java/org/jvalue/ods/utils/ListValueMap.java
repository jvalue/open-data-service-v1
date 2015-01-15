package org.jvalue.ods.utils;

import com.google.common.base.Objects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Map that can store multiple values for one key.
 */
public final class ListValueMap<K, V> {

	private final Map<K, List<V>> data = new HashMap<>();


	public void add(K key, V value) {
		List<V> values = data.get(key);
		if (values == null) {
			values = new LinkedList<>();
			data.put(key, values);
		}
		values.add(value);
	}


	public List<V> get(K key) {
		return data.get(key);
	}


	public Map<K, List<V>> getAll() {
		return data;
	}


	public boolean contains(K key) {
		return data.containsKey(key);
	}


	public boolean contains(K key, V value) {
		if (data.get(key) == null) return false;
		return data.get(key).contains(value);
	}


	public boolean isEmpty() {
		return data.isEmpty();
	}


	public int size() {
		int size = 0;
		for (Map.Entry<K, List<V>> entry : data.entrySet()) size += entry.getValue().size();
		return size;
	}


	public boolean remove(K key, V value) {
		List<V> values = data.get(key);
		if (values == null) return false;
		if (!values.remove(value)) return false;
		if (values.isEmpty()) data.remove(key);
		return true;
	}


	public boolean remove(K key) {
		if (!data.containsKey(key)) return false;
		data.remove(key);
		return true;
	}


	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ListValueMap)) return false;
		if (other == this) return true;
		ListValueMap map = (ListValueMap) other;
		return Objects.equal(data, map.data);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(data);
	}

}
