package org.jvalue.ods.rest.v2.jsonapi.resource;

import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;
import org.jvalue.ods.rest.v2.jsonapi.resource.JsonApiResourceIdentifier;

import java.net.URI;
import java.util.Map;

public abstract class JsonApiData {

	protected final URI uri;
	protected final String id;
	protected final String type;
	protected Map<String, URI> links;

	public JsonApiData(URI uri, JsonApiIdentifiable entity) {
		this.uri = uri;
		this.id = entity.getId();
		this.type = entity.getClass().getSimpleName();
	}

	public abstract JsonApiResourceIdentifier toIdentifier();
}
