package org.jvalue.ods.db.mongodb.repositories;

/**
 * Describes a repository capable of dealing with wrapper
 * Java objects.
 * @param <D> type of the wrapper class
 * @param <V> type of the object contained within the wrapper
 */
public interface GenericDbDocumentAdaptable<V> {

	public V findById(String id);
	public V createDbDocument(V value);
	public String getIdForValue(V value);

}
