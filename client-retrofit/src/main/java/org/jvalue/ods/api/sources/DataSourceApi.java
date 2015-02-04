package org.jvalue.ods.api.sources;


import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface DataSourceApi {

	static final String URL_DATASOURCES = "/datasources";

	@GET(URL_DATASOURCES + "/{sourceId}")
	public DataSource get(@Path("sourceId") String sourceId);


	@GET(URL_DATASOURCES + "/{sourceId}/schema")
	public JsonNode getSchema(@Path("sourceId") String sourceId);


	@GET(URL_DATASOURCES)
	public List<DataSource> getAll();


	@PUT(URL_DATASOURCES + "/{sourceId}")
	public DataSource add(@Path("sourceId") String sourceId, @Body DataSourceDescription source);


	@DELETE(URL_DATASOURCES + "/{sourceId}")
	public Response remove(@Path("sourceId") String sourceId);


}
