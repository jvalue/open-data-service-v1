package org.jvalue.ods.rest.v2.jsonapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jvalue.commons.utils.Assert;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import javax.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.net.URI;
import java.util.*;

import static org.jvalue.ods.utils.HttpUtils.appendTrailingSlash;

public class JsonApiDocument implements Serializable, JsonLinks {

	private UriInfo uriInfo;

	private final Map<String, URI> links = new HashMap<>();

	protected JsonApiData data;

	protected List<JsonApiError> errors;

	private List<JsonApiResource> included;


	public JsonApiDocument(JsonApiError error) {
		Assert.assertTrue(data == null);

		errors = new LinkedList<>();
		errors.add(error);
	}


	public JsonApiDocument(JsonApiIdentifiable entity,
						   UriInfo uriInfo) {
		Assert.assertTrue(errors == null);
		Assert.assertTrue(included == null);

		this.uriInfo = uriInfo;
		this.data = new JsonApiResource(entity, uriInfo.getAbsolutePath());
	}


	public JsonApiDocument(Collection<? extends JsonApiIdentifiable> entityCollection,
						   UriInfo uriInfo) {
		Assert.assertTrue(errors == null);

		this.uriInfo = uriInfo;
		URI collectionURI = appendTrailingSlash(uriInfo.getAbsolutePath());

		data = new JsonApiResourceCollection(entityCollection, collectionURI);
	}


	public void setResourceCollectionURI(URI collectionURI) {
		data.setResourceCollectionURI(collectionURI);
	}


	@JsonInclude(JsonInclude.Include.NON_NULL)
	public JsonApiData getData() {
		return data;
	}


	@JsonInclude(JsonInclude.Include.NON_NULL)
	public List<JsonApiError> getErrors() {
		return errors;
	}


	@JsonInclude(JsonInclude.Include.NON_NULL)
	public List<JsonApiResource> getIncluded() {
		return included;
	}


	private URI getRelationshipURI(JsonApiIdentifiable relationship) {
		return data.getRelationshipUri(relationship);
	}


	public boolean hasRelationshipTo(JsonApiIdentifiable entity) {
		return data.hasRelationshipTo(entity);
	}


	public void restrictTo(String attribute) {
		data.restrictTo(attribute);
	}


	public void addRelationship(String name, JsonApiIdentifiable entity, URI location) {
		data.addRelationship(name, entity, location);
	}


	public void addRelationship(String name, Collection<? extends JsonApiIdentifiable> entity, URI location) {
		data.addRelationship(name, entity, location);
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
		return uriInfo.getRequestUri();
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
