package org.jvalue.ods.data;

public class NumberValue extends GenericValue {

	private static final long serialVersionUID = 1L;

	private Number number;
	private final String type = "number";

	public NumberValue(Number number) {
		this.number = number;
	}

	public Number getNumber() {
		return number;
	}

	public String getType() {
		return type;
	}

}
