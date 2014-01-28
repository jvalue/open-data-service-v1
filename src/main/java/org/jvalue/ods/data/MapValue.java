package org.jvalue.ods.data;

import java.util.HashMap;
import java.util.Map;

public class MapValue extends GenericValue {

	private static final long serialVersionUID = 1L;

	private Map<String, GenericValue> map = new HashMap<String, GenericValue>();

	public MapValue(Map<String, GenericValue> map) {
		this.map = map;
	}
	
	public Map<String, GenericValue> getMap() {
		return map;
	}

}
