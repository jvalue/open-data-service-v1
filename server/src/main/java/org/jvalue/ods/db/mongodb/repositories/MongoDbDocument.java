package org.jvalue.ods.db.mongodb.repositories;


import org.bson.Document;

public class MongoDbDocument<V> extends Document {

	private V value;


	protected MongoDbDocument(V value) {
		this.value = value;
	}


	public V getValue() {
		return value;
	}


	public void setValue(V value) {
		this.value = value;
	}

}
