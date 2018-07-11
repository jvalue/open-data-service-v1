package org.jvalue.ods.rest.v2.jsonapi;

import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import javax.ws.rs.core.UriInfo;
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

	protected abstract JsonApiResourceIdentifier toIdentifier();
}
