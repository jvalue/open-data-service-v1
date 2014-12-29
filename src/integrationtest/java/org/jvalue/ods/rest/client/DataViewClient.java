package org.jvalue.ods.rest.client;


import com.fasterxml.jackson.databind.JsonNode;

import org.jvalue.ods.rest.model.DataView;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface DataViewClient {

	static String VIEWS_URL = BaseClient.BASE_URL + "/datasources/{sourceId}/views";

	@GET(VIEWS_URL)
	public List<DataView> getAll(@Path("sourceId") String sourceId);


	@GET(VIEWS_URL + "/{id}?execute=false")
	public DataView get(@Path("sourceId") String sourceId, @Path("id") String id);


	@GET(VIEWS_URL + "/{id}?execute=true")
	public JsonNode execute(@Path("sourceId") String sourceId, @Path("id") String id);


	@GET(VIEWS_URL + "/{id}?execute=true")
	public JsonNode execute(@Path("sourceId") String sourceId, @Path("id") String id, @Query("argument") String argument);


	@DELETE(VIEWS_URL + "/{id}")
	public Response remove(@Path("sourceId") String sourceId, @Path("id") String id);


	@PUT(VIEWS_URL + "/{id}")
	public DataView add(@Path("sourceId") String sourceId, @Path("id") String id, @Body DataView dataView);

}
