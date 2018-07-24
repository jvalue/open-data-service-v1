package org.jvalue.ods.rest.v2.jsonapi.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import java.net.URI;
import java.util.*;

import static org.jvalue.ods.rest.v2.jsonapi.document.JsonLinks.RELATED;

public class JsonApiRelationship {

	@JsonFormat(with = JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
	private final List<JsonApiIdentifiable> data = new LinkedList<>();

	private final Map<String, URI> links = new HashMap<>();


	public JsonApiRelationship(JsonApiIdentifiable entity, URI location) {
		data.add(entity);
		links.put(RELATED, location);
	}


	public JsonApiRelationship(Collection<? extends JsonApiIdentifiable> entityCollection, URI location) {
		data.addAll(entityCollection);
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
	public boolean containsEntity(JsonApiIdentifiable entity) {
		return data.stream()
			.anyMatch(id -> id.equals(entity));
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
