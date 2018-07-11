package org.jvalue.ods.rest.v2.jsonapi;

import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import javax.ws.rs.core.UriInfo;

public class SingleObjectDocument extends JsonApiDocument {

    private final JsonApiResource data;

    public SingleObjectDocument(JsonApiIdentifiable data, UriInfo uriInfo) {
        super(uriInfo);
        this.data = new JsonApiResource(data, uriInfo.getAbsolutePath());
    }

    @Override
    public Object getData() {
        return data;
    }
}
