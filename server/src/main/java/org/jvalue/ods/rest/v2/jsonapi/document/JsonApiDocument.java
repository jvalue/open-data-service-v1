package org.jvalue.ods.rest.v2.jsonapi.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.*;

public abstract class JsonApiDocument {

	protected Map<String, URI> links = new HashMap<>();
	protected final UriInfo uriInfo;

	public JsonApiDocument(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
		links.put("self", uriInfo.getAbsolutePath());
	}

	public Map<String, URI> getLinks() {
		return links;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("data")
	public abstract Object getData();


	/**
	 * if called, data is serialized using only type, id and selflink (JsonApiIdentifierObject)
	 */
	public abstract JsonApiDocument toIdentifier();


	public void addLink(String name, URI ref) {
		links.put(name, ref);
	}


}
