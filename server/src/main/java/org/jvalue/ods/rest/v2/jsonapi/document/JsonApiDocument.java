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
		links.put("self", uriInfo.getAbsolutePath());
	}

	public JsonApiDocument(Collection<? extends JsonApiIdentifiable> entityCollection, UriInfo uriInfo) {
		this.uriInfo = uriInfo;
		URI uri = uriInfo.getAbsolutePath();
		entityCollection.forEach(
				entity ->
						data.add(new JsonApiResource(entity, uri.resolve(entity.getId())))
		);
	}

	public void toIdentifier() {

		List<JsonApiResourceIdentifier> identifierList = data
				.stream()
				.map(resource -> resource.toIdentifier())
				.collect(Collectors.toList());

		data = identifierList;
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

}
