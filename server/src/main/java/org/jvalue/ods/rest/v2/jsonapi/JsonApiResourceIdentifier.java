package org.jvalue.ods.rest.v2.jsonapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;


public class JsonApiResourceIdentifier extends JsonApiData {


	public JsonApiResourceIdentifier(JsonApiIdentifiable entity, URI uri) {
		super(uri, entity);
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Map<String, URI> getLinks() {
		return links;
	}

	@Override
	protected JsonApiResourceIdentifier toIdentifier() {
		return this;
	}
}
