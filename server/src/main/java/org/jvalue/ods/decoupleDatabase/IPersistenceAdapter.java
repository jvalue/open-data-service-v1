package org.jvalue.ods.decoupleDatabase;

import java.util.List;

public interface IPersistenceAdapter<V> {
	V findById(String Id);
	void update(V value);
	void add(V value);
	void remove(V value);
	List<V> getAll();
}
