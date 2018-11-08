package org.jvalue.ods.rest.v2.api;



import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import static org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse.JSONAPI_TYPE;


@Produces(JSONAPI_TYPE)
@Consumes(JSONAPI_TYPE)
public abstract class AbstractApi {

	public static final String API_VERSION = "2.0.0";

	public static final String DATASOURCES = "datasources";
	public static final String FILTERTYPES = "filterTypes";
	public static final String USERS = "users";
	public static final String DATA = "data";
	public static final String FILTERCHAINS = "filterChains";
	public static final String PLUGINS = "plugins";
	public static final String NOTIFICATIONS = "notifications";
	public static final String VIEWS = "views";
	public static final String ENTRYPOINT = "entryPoint";
	public static final String DOC = "doc";

	public static final String VIEW = "view";
	public static final String DATASOURCE = "datasource";

	public static final String BASICAUTH = "basicAuth";

	protected static final String V2 = "/v2";

	protected static final String BASE_URL = V2 + "/" + DATASOURCES;

	protected static WebApplicationException createJsonApiException(String message, int code) {
		return new WebApplicationException(
				JsonApiResponse
						.createExceptionResponse(code)
						.message(message)
						.build());
	}
}
