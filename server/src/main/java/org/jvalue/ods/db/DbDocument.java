package org.jvalue.ods.db;


import org.ektorp.support.CouchDbDocument;

/**
 * Wrapper object for storing regular Java objects in CouchDb without
 * adding an _id and _rev field with them.
 *
 * This way external objects can be store in the db.
 *
 * @param <V> type of the object contained within this wrapper.
 */
class DbDocument<V> extends CouchDbDocument {

	private V value;

	protected DbDocument(V value) {
		this.value = value;
	}


	public V getValue() {
		return value;
	}


	public void setValue(V value) {
		this.value = value;
	}

}
