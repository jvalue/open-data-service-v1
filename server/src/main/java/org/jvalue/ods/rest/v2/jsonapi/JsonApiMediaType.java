package org.jvalue.ods.rest.v2.jsonapi;


import javax.ws.rs.core.MediaType;

public class JsonApiMediaType extends MediaType {

    public static final String JSONAPI = "application/vnd.api+json";
    public static final MediaType JSONAPI_TYPE = new MediaType("application", "vnd.api+json");
}
