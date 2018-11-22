package api;

import jsonapi.ResponseBody;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import retrofit.client.Response;
import retrofit.http.*;

public interface NotificationApi {
	String BASE_URL = "/v2/{sourceId}/notifications";

	@GET(BASE_URL + "/{clientId}")
	@Headers("Content-type: " + JsonApiResponse.JSONAPI_TYPE)
	ResponseBody getClient(@Path("sourceId") String sourceId, @Path("clientId") String clientId);

	@POST(BASE_URL)
	@Headers("Content-type: " + JsonApiResponse.JSONAPI_TYPE)
	ResponseBody registerClient(@Path("sourceId") String sourceId, @Body JsonApiRequest clientDescription);

	@DELETE(BASE_URL + "/{clientId}")
	@Headers("Content-type: " + JsonApiResponse.JSONAPI_TYPE)
	Response unregisterClient(@Path("sourceId") String sourceId, @Path("clientId") String clientId);
}
