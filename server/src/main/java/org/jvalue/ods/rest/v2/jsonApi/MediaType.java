package org.jvalue.ods.rest.v2.jsonApi;


public class MediaType extends javax.ws.rs.core.MediaType{

    public static final String JSONAPI = "application/vnd.api+json";
    public static final MediaType JSONAPI_TYPE = new MediaType("application", "vnd.api+json");

    public MediaType(String type,
                     String subtype) {
        super(type, subtype);
    }
}
