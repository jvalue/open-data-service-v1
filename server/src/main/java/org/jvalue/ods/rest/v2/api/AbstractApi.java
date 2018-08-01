package org.jvalue.ods.rest.v2.api;


import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import static org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse.JSONAPI_TYPE;


@Produces(JSONAPI_TYPE)
@Consumes(JSONAPI_TYPE)
abstract class AbstractApi {

	protected static final String VERSION = "/v2";

	protected static final String BASE_URL = VERSION + "/datasources";
}
