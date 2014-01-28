package org.jvalue.ods.data;

import java.util.LinkedList;


public class ListValueType extends ValueType<LinkedList<?>, String> {

	private static final long serialVersionUID = 1L;

	public ListValueType(LinkedList<?> value, String type) {
		super(value, type);		
	}
}
