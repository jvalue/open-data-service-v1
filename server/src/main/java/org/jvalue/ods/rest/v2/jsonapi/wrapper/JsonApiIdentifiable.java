package org.jvalue.ods.rest.v2.jsonapi.wrapper;

public interface JsonApiIdentifiable {
	String getId();
	String getType();

	default boolean isSame(JsonApiIdentifiable other) {
		return getId().equals(other.getId()) && getType().equals(other.getType());
	}
}
