package org.jvalue.ods.data;

import java.util.LinkedHashMap;

public class MapValueType extends ValueType<LinkedHashMap<?,?>, String> {

	private static final long serialVersionUID = 1L;

	public MapValueType(LinkedHashMap<?,?> value, String type) {
		super(value, type);		
	}

	
	
}
