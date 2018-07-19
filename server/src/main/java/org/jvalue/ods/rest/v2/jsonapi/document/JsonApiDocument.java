package org.jvalue.ods.rest.v2.jsonapi.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import javax.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class JsonApiDocument implements Serializable, JsonLinks {

	private final UriInfo uriInfo;

	private final Map<String, URI> links = new HashMap<>();

	@JsonFormat(with = {
			JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED,
			JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY})
	protected List<JsonApiResourceIdentifier> data = new LinkedList<>();


	public JsonApiDocument(JsonApiIdentifiable entity, UriInfo uriInfo) {
		this.uriInfo = uriInfo;
		data.add(new JsonApiResource(entity, uriInfo.getAbsolutePath()));
		addSelfLink(uriInfo);
	}


	public JsonApiDocument(Collection<? extends JsonApiIdentifiable> entityCollection, UriInfo uriInfo) {
		this.uriInfo = uriInfo;

		for (JsonApiIdentifiable entity : entityCollection) {
			URI entityUri = uriInfo.getAbsolutePath().resolve(entity.getId());
			JsonApiResource resource = new JsonApiResource(entity, entityUri);
			resource.addSelfLink(entityUri);
			data.add(resource);
		}
		addSelfLink(uriInfo);
	}


	public void toIdentifier() {
		data = data
				.stream()
				.map(JsonApiResourceIdentifier::toIdentifier)
				.collect(Collectors.toList());
	}

	public List<JsonApiResourceIdentifier> getData() {
		return data;
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
