package org.jvalue.ods.db;


import org.ektorp.support.CouchDbDocument;

abstract class DbDocument<V> extends CouchDbDocument {

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
