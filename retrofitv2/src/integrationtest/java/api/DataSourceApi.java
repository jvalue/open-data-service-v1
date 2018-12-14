package api;


import jsonapi.ResponseBody;
import org.jvalue.ods.rest.v2.jsonapi.request.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import retrofit.client.Response;
import retrofit.http.*;

public interface DataSourceApi {
	String BASE_URL = "/v2/datasources";

	@GET(BASE_URL)
	@Headers("Content-type: " + JsonApiResponse.JSONAPI_TYPE)
	ResponseBody getAllSources();

	@GET(BASE_URL + "/{sourceId}")
	@Headers("Content-type: " + JsonApiResponse.JSONAPI_TYPE)
	ResponseBody getSource(@Path("sourceId") String sourceId);

	@GET(BASE_URL + "/{sourceId}/schema")
	@Headers("Content-type: " + JsonApiResponse.JSONAPI_TYPE)
	ResponseBody getSourceSchema(@Path("sourceId") String sourceId);

	@POST(BASE_URL)
	@Headers("Content-type: " + JsonApiResponse.JSONAPI_TYPE)
	ResponseBody addSource(@Body JsonApiRequest sourceDescription);

	@DELETE(BASE_URL + "/{sourceId}")
	Response deleteSource(@Path("sourceId") String sourceId);
}
