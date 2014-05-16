package org.jvalue.ods.qa.valueTypes;

import java.util.Map;
import java.util.Map.Entry;

import org.jvalue.ValueType;

public class MapType<E> extends ValueType<Map<String, E>> {

	private Map<String, ValueType<E>> typeMap;

	public MapType(Map<String, ValueType<E>> typeMap) {
		this.typeMap = typeMap;
	}

	@Override
	public boolean isValidInstance(Map<String, E> map) {

		for (Entry<String, E> e : map.entrySet()) {

			ValueType<E> k = typeMap.get(e.getKey());

			if (k == null) {
				continue;
				// if additional value is ok
			}

			if (!k.isValidInstance(e.getValue())) {
				return false;
			}
		}

		return true;
	}
}