/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v2.jsonapi.response;

import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import java.util.Objects;

public class JsonApiResourceIdentifier implements JsonApiIdentifiable {

	private final String id;
	private final String type;

	JsonApiResourceIdentifier(JsonApiIdentifiable entity) {
		this.id = entity.getId();
		this.type = entity.getType();
	}


	JsonApiResourceIdentifier(String id, String type) {
		this.id = id;
		this.type = type;
	}


	@Override
	public String getId() {
		return id;
	}


	@Override
	public String getType() {
		return type;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		JsonApiResourceIdentifier that = (JsonApiResourceIdentifier) o;
		return Objects.equals(id, that.id) &&
			Objects.equals(type, that.type);
	}


	@Override
	public int hashCode() {
		return Objects.hash(id, type);
	}
}
