package org.jvalue.ods.data;

import java.io.Serializable;

public class Value implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Value value;
	private final String type;

	public Value(Value value, String type) {
		this.value = value;
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public Value getValue() {
		return value;
	}

}
