package org.jvalue.ods.rest.v2;


import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiMediaType;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

@Produces(JsonApiMediaType.JSONAPI)
@Consumes(JsonApiMediaType.JSONAPI)
abstract class AbstractApi {


	protected static final String BASE_URL = "/v2/datasources/";
}
