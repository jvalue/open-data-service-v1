package org.jvalue.ods.rest.v2.jsonapi.response;

public class JsonApiError {
	private final String message;
	private final int code;

	protected JsonApiError(String message, int code) {
		this.message = message;
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}
}
