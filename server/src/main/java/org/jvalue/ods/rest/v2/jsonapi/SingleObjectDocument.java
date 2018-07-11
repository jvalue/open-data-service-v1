package org.jvalue.ods.rest.v2.jsonapi;

import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import javax.ws.rs.core.UriInfo;

public class SingleObjectDocument extends JsonApiDocument {


	private final JsonApiData data;


	public SingleObjectDocument(JsonApiIdentifiable data, UriInfo uriInfo) {
		super(uriInfo);
		this.data = new JsonApiResource(data, uriInfo.getAbsolutePath());
	}

	public SingleObjectDocument(JsonApiData data, UriInfo uriInfo) {
		super(uriInfo);
		this.data = data;
	}

	@Override
	public JsonApiDocument toIdentifier() {
		return new SingleObjectDocument(data.toIdentifier(), uriInfo);
	}


	@Override
	public Object getData() {
		return data;
	}
}
