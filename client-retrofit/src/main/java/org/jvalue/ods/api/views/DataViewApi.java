package org.jvalue.ods.api.views;


import com.fasterxml.jackson.databind.JsonNode;

import org.jvalue.ods.api.sources.DataSourceApi;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface DataViewApi {

	static String URL_VIEWS = DataSourceApi.URL_DATASOURCES + "/{sourceId}/views";

	@GET(URL_VIEWS)
	public List<DataView> getAll(@Path("sourceId") String sourceId);


	@GET(URL_VIEWS + "/{id}?execute=false")
	public DataView get(@Path("sourceId") String sourceId, @Path("id") String id);


	@GET(URL_VIEWS + "/{id}?execute=true")
	public JsonNode execute(@Path("sourceId") String sourceId, @Path("id") String id);


	@GET(URL_VIEWS + "/{id}?execute=true")
	public JsonNode execute(@Path("sourceId") String sourceId, @Path("id") String id, @Query("argument") String argument);


	@DELETE(URL_VIEWS + "/{id}")
	public Response remove(@Path("sourceId") String sourceId, @Path("id") String id);


	@PUT(URL_VIEWS + "/{id}")
	public DataView add(@Path("sourceId") String sourceId, @Path("id") String id, @Body DataViewDescription dataView);

}
