package org.jvalue.ods.rest.v2.jsonapi.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import javax.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class JsonApiDocument implements Serializable, JsonLinks {

	private UriInfo uriInfo;

	private final Map<String, URI> links = new HashMap<>();

	@JsonFormat(with = {
			JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED,
			JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY})
	protected List<JsonApiResource> data = new LinkedList<>();

	private final List<JsonApiResource> included = new LinkedList<>();


	public JsonApiDocument(JsonApiIdentifiable entity, UriInfo uriInfo) {
		this.uriInfo = uriInfo;
		data.add(new JsonApiResource(entity, uriInfo.getAbsolutePath()));
	}


	public JsonApiDocument(Collection<? extends JsonApiIdentifiable> entityCollection, UriInfo uriInfo) {
		this.uriInfo = uriInfo;

		for (JsonApiIdentifiable entity : entityCollection) {
			URI entityUri = uriInfo.getAbsolutePath().resolve(entity.getId());
			JsonApiResource resource = new JsonApiResource(entity, entityUri);
			resource.addSelfLink();
			data.add(resource);
		}
	}


	public List<JsonApiResource> getData() {
		return data;
	}


	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public List<JsonApiResource> getIncluded() {
		return included;
	}


	private URI getRelationshipURI (JsonApiIdentifiable relationship) {
		URI relationshipURI = null;

		for(JsonApiResource dataElement: data) {
			if (dataElement.hasRelationshipTo(relationship)) {
				relationshipURI = dataElement.getRelationshipUri(relationship);
			}
		}

		if(relationshipURI == null) {
			throw new IllegalArgumentException("relationship " + relationship.getId() + " does not exist.");
		}

		return relationshipURI;
	}


	public boolean hasRelationshipTo(JsonApiIdentifiable entity) {
		return data.stream()
			.anyMatch(e -> e.hasRelationshipTo(entity));
	}


	public void restrictTo(String attribute) {
		data = data
			.stream()
			.map(r -> r.restrictTo(attribute))
			.collect(Collectors.toList());
	}


	public void addRelationship(String name, JsonApiIdentifiable entity, URI location) {
		data = data
			.stream()
			.map(r -> r.addRelationship(name, entity, location))
			.collect(Collectors.toList());
	}


	public void addRelationship(String name, Collection<? extends JsonApiIdentifiable> entity, URI location) {
		data = data
			.stream()
			.map(r -> r.addRelationship(name, entity, location))
			.collect(Collectors.toList());
	}


	public void addIncluded(JsonApiIdentifiable entity) {
		URI location = getRelationshipURI(entity);
		JsonApiResource includedResource = new JsonApiResource(entity, location);
		includedResource.addSelfLink();
		included.add(includedResource);
	}


	@Override
	public Map<String, URI> getLinks() {
		return links;
	}


	@Override
	public void addLink(String name, URI ref) {
		links.put(name, ref);
	}


	@Override
	public URI getURI() {
		return uriInfo.getAbsolutePath();
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		JsonApiDocument that = (JsonApiDocument) o;
		return Objects.equals(links, that.links) &&
				Objects.equals(uriInfo, that.uriInfo) &&
				Objects.equals(data, that.data);
	}


	@Override
	public int hashCode() {

		return Objects.hash(links, uriInfo, data);
	}
}
