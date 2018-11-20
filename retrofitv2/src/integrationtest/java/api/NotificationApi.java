package api;

import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiDocument;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import retrofit.client.Response;
import retrofit.http.*;

public interface NotificationApi {
	String BASE_URL = "/v2/{sourceId}/notifications";

	@GET(BASE_URL + "/{clientId}")
	@Headers("Content-type: " + JsonApiResponse.JSONAPI_TYPE)
	JsonApiDocument getClient(@Path("sourceId") String sourceId, @Path("clientId") String clientId);

	@POST(BASE_URL)
	@Headers("Content-type: " + JsonApiResponse.JSONAPI_TYPE)
	JsonApiDocument registerClient();

	@DELETE(BASE_URL + "/{clientId}")
	@Headers("Content-type: " + JsonApiResponse.JSONAPI_TYPE)
	Response unregisterClient(@Path("sourceId") String sourceId, @Path("clientId") String clientId);
}
