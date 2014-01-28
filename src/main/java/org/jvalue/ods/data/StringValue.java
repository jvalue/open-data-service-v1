package org.jvalue.ods.data;

public class StringValue extends GenericValue {

	private static final long serialVersionUID = 1L;

	private String s;
	private final String type = "string";

	public StringValue(String s) {
		this.s = s;
	}

	public String getString() {
		return s;
	}

	public String getType() {
		return type;
	}

}
