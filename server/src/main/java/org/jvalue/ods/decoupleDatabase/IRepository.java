package org.jvalue.ods.decoupleDatabase;

import java.util.List;

public interface IRepository<V> {
	V findById(String Id);
	void add(V Value);
	void update(V value);
	void remove(V Value);
	List<V> getAll();
}
