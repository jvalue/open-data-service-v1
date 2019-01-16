/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v2.jsonapi.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import java.net.URI;
import java.util.*;

import static org.jvalue.ods.rest.v2.jsonapi.response.JsonLinks.RELATED;

public class JsonApiRelationship {

	@JsonFormat(with = JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
	private final List<JsonApiIdentifiable> data = new LinkedList<>();

	private final Map<String, URI> links = new HashMap<>();


	JsonApiRelationship(JsonApiIdentifiable entity, URI location) {
		data.add(new JsonApiResourceIdentifier(entity));
		links.put(RELATED, location);
	}


	JsonApiRelationship(Collection<? extends JsonApiIdentifiable> entityCollection, URI location) {
		entityCollection.forEach(entity -> data.add(new JsonApiResourceIdentifier(entity)));
		links.put(RELATED, location);
	}


	public List<JsonApiIdentifiable> getData() {
		return data;
	}


	public Map<String, URI> getLinks() {
		return links;
	}


	/**
	 * Checks whether a relation to a certain JsonApiIdentifiable exists,
	 * more formally, returns true iff this relationships data list contains at least one
	 * element which has the same type and id as the given entity
	 *
	 * @param entity the entity to check
	 * @return true if a relation to entity exists, false otherwise
	 */
	boolean containsEntity(JsonApiIdentifiable entity) {
		return data.stream()
			.anyMatch(id -> id.isSame(entity));
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		JsonApiRelationship that = (JsonApiRelationship) o;
		return Objects.equals(data, that.data) &&
			Objects.equals(links, that.links);
	}


	@Override
	public int hashCode() {

		return Objects.hash(data, links);
	}
}
