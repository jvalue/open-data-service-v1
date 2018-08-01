package org.jvalue.ods.rest.v2.api;


import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import static org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse.JSONAPI_TYPE;


@Produces(JSONAPI_TYPE)
@Consumes(JSONAPI_TYPE)
abstract class AbstractApi {

	static final String DATASOURCES = "datasources";
	static final String FILTERTYPES = "filterTypes";
	static final String USERS = "users";
	static final String DATA = "data";
	static final String FILTERCHAINS = "filterChains";
	static final String PLUGINS = "plugins";
	static final String NOTIFICATIONS = "notifications";
	static final String VIEWS = "views";
	static final String ENTRYPOINT = "entryPoint";
	static final String VERSION = "version";

	protected static final String V2 = "/v2";

	protected static final String BASE_URL = V2 + "/" + DATASOURCES;

}
