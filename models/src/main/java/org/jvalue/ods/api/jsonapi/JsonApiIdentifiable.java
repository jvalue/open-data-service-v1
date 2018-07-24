package org.jvalue.ods.api.jsonapi;

public interface JsonApiIdentifiable {
	String getId();
	String getType();

	default boolean isSame(JsonApiIdentifiable other) {
		return getId().equals(other.getId()) && getType().equals(other.getType());
	}
}
