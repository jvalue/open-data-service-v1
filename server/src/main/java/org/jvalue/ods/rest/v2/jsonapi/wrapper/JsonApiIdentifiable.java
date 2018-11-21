package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface JsonApiIdentifiable {

	@JsonIgnore
	String getId();

	@JsonIgnore
	String getType();

	default boolean isSame(JsonApiIdentifiable other) {
		return getId().equals(other.getId()) && getType().equals(other.getType());
	}
}
