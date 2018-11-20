package api;


import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiDocument;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.DataSourceWrapper;
import retrofit.client.Response;
import retrofit.http.*;

public interface DataSourceApi {
	String BASE_URL = "/v2/datasources";

	@GET(BASE_URL)
	@Headers("Content-type: " + JsonApiResponse.JSONAPI_TYPE)
	JsonApiDocument getAllSources();

	@GET(BASE_URL + "/{sourceId}")
	@Headers("Content-type: " + JsonApiResponse.JSONAPI_TYPE)
	JsonApiDocument getSource(@Path("sourceId") String sourceId);

	@POST(BASE_URL)
	@Headers("Content-type: " + JsonApiResponse.JSONAPI_TYPE)
	JsonApiDocument addSource(@Body JsonApiRequest sourceDescription);

	@DELETE(BASE_URL + "/{sourceId}")
	Response deleteSource(@Path("sourceId") String sourceId);
}
