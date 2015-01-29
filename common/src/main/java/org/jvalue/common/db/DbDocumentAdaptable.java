package org.jvalue.common.db;


/**
 * Describes a repository capable of dealing with wrapper
 * Java objects.
 * @param <D> type of the wrapper class
 * @param <V> type of the object contained within the wrapper
 */
public interface DbDocumentAdaptable<D extends DbDocument<V>, V> {

	public D findById(String id);
	public D createDbDocument(V value);
	public String getIdForValue(V value);

}
