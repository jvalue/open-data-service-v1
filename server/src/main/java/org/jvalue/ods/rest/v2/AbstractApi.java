package org.jvalue.ods.rest.v2;


import org.jvalue.ods.rest.v2.jsonApi.MediaType;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

@Produces(MediaType.JSONAPI)
@Consumes(MediaType.JSONAPI)
abstract class AbstractApi {


	protected static final String BASE_URL = "/v2/datasources";
}
