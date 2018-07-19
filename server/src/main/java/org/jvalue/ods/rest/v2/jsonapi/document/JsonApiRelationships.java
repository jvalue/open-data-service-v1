package org.jvalue.ods.rest.v2.jsonapi.document;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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


	private class Relationship {

		@JsonFormat(with = JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
		private final List<JsonApiResourceIdentifier> data = new LinkedList<>();

		private final Map<String, URI> links = new HashMap<>();


		public Relationship(JsonApiIdentifiable entity, URI location) {
			data.add(new JsonApiResourceIdentifier(entity));
			links.put(RELATED, location);
		}


		public List<JsonApiResourceIdentifier> getData() {
			return data;
		}


		public Map<String, URI> getLinks() {
			return links;
		}
	}
}
