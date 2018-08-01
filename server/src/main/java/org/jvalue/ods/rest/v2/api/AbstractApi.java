package org.jvalue.ods.rest.v2.api;


import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import static org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse.JSONAPI_TYPE;


@Produces(JSONAPI_TYPE)
@Consumes(JSONAPI_TYPE)
abstract class AbstractApi {

	protected static final String DATASOURCES = "datasources";
	protected static final String FILTERTYPES = "filterTypes";
	protected static final String USERS = "users";
	protected static final String DATA = "data";
	protected static final String FILTERCHAINS = "filterChains";
	protected static final String PLUGINS = "plugins";
	protected static final String NOTIFICATIONS = "notifications";
	protected static final String VIEWS = "views";

	protected static final String VERSION = "/v2";

	protected static final String BASE_URL = VERSION + "/" + DATASOURCES;

}
