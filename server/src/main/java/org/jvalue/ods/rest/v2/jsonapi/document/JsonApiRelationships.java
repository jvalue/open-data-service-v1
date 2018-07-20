package org.jvalue.ods.rest.v2.jsonapi.document;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import java.net.URI;
import java.util.*;

import static org.jvalue.ods.rest.v2.jsonapi.document.JsonLinks.RELATED;

public class JsonApiRelationships {

	private final Map<String, Relationship> relationships = new HashMap<>();


	@JsonAnyGetter
	public Map<String, Relationship> getRelationships() {
		return relationships;
	}


	public void addRelationship(String name, JsonApiIdentifiable entity, URI location) {
		relationships.put(name, new Relationship(entity, location));
	}


	public boolean hasRelationshipTo(JsonApiIdentifiable other) {

		for (Map.Entry<String, Relationship> entry: relationships.entrySet()) {
			if(entry.getValue().containsEntity(other)) {
				return true;
			}
		}

		return false;
	}

	class Relationship {

		@JsonFormat(with = JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
		private final List<JsonApiResourceIdentifier> data = new LinkedList<>();

		private final Map<String, URI> links = new HashMap<>();


		private Relationship(JsonApiIdentifiable entity, URI location) {
			data.add(new JsonApiResourceIdentifier(entity));
			links.put(RELATED, location);
		}


		public List<JsonApiResourceIdentifier> getData() {
			return data;
		}


		public Map<String, URI> getLinks() {
			return links;
		}


		/**
		 * Checks whether a relation to a certain JsonApiIdentifiable exists,
		 * more formally, returns true iff this relationships data list contains at least one
		 * element which has the same type and id as the given entity
		 * @param entity the entity to check
		 * @return true if a relation to entity exists, false otherwise
		 */
		private boolean containsEntity(JsonApiIdentifiable entity) {
			for(JsonApiResourceIdentifier identifier: data) {
				if(identifier.getId().equals(entity.getId())
					&& identifier.getType().equals(entity.getClass().getSimpleName())) {
					return true;
				}
			}
			return false;
		}
	}
}
