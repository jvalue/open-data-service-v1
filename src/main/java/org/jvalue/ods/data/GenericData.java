package org.jvalue.ods.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GenericData implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Map<String, Value> attributes;

	public GenericData() {
		attributes = new HashMap<String, Value>();
	}

	public GenericData(Map<String, Value> attributes) {
		this.attributes = attributes;
	}

	public void addAttribute(String keyName, Value valueType) {
		attributes.put(keyName, valueType);
	}

	public Value getAttribute(String keyName) {
		return attributes.get(keyName);
	}
}
