package org.jvalue.ods.data;

import java.io.Serializable;

public class ValueType<V, T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final V value;
	private final T type;
	
	public ValueType(V value, T type) {
	    this.value = value;
	    this.type = type;	    
	}

	public T getType() {
		return type; 
	}
	
	public V getValue() {
		return value; 
	}
	
}
