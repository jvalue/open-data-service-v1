package org.jvalue.ods.db;


public interface DbDocumentAdaptable<D extends DbDocument<V>, V> {

	public D findById(String id);
	public D createDbDocument(V value);
	public String getIdForValue(V value);

}
