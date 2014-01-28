package org.jvalue.ods.data;

import java.util.LinkedList;
import java.util.List;

public class ListValue extends GenericValue {

	private static final long serialVersionUID = 1L;

	private List<GenericValue> list = new LinkedList<GenericValue>();

	public ListValue(List<GenericValue> list) {
		this.list = list;
	}

	public List<GenericValue> getList() {
		return list;
	}

}
