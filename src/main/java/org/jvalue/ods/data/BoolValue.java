package org.jvalue.ods.data;

public class BoolValue extends GenericValue {

	private static final long serialVersionUID = 1L;

	private boolean bool;
	private final String type = "bool";

	public BoolValue(boolean bool) {
		this.bool = bool;
	}

	public boolean getBool() {
		return bool;
	}

	public String getType() {
		return type;
	}

}
