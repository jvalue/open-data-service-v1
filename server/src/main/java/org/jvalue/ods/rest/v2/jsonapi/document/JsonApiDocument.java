package org.jvalue.ods.rest.v2.jsonapi.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import javax.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class JsonApiDocument implements Serializable {

	private final Map<String, URI> links = new HashMap<>();
	private final UriInfo uriInfo;
	@JsonFormat(with = {JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY})
	protected List<JsonApiResourceIdentifier> data = new LinkedList<>();

	public JsonApiDocument(JsonApiIdentifiable entity, UriInfo uriInfo) {
		this.uriInfo = uriInfo;
		data.add(new JsonApiResource(entity, uriInfo.getAbsolutePath()));
		addSelfLink();
	}


	public JsonApiDocument(Collection<? extends JsonApiIdentifiable> entityCollection, UriInfo uriInfo) {
		this.uriInfo = uriInfo;
		URI uri = uriInfo.getAbsolutePath();
		entityCollection.forEach(
				entity ->
						data.add(new JsonApiResource(entity, uri.resolve(entity.getId())))
		);
		addSelfLink();
	}


	public void toIdentifier() {
		data = data
				.stream()
				.map(JsonApiResourceIdentifier::toIdentifier)
				.collect(Collectors.toList());
	}

	public void addLink(String name, URI ref) {
		links.put(name, ref);
	}


	public Map<String, URI> getLinks() {
		return links;
	}


	public List<JsonApiResourceIdentifier> getData() {
		return data;
	}


	private void addSelfLink() {
		addLink("self", uriInfo.getAbsolutePath());
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
