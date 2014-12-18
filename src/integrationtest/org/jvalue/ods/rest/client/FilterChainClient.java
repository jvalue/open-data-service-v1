package org.jvalue.ods.rest.client;


import org.jvalue.ods.rest.model.ProcessorChainReference;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface FilterChainClient {

	static String FILTER_URL = BaseClient.BASE_URL + "/datasources/{sourceId}/filterChains";

	@GET(FILTER_URL)
	public List<ProcessorChainReference> getAll(@Path("sourceId") String sourceId);


	@GET(FILTER_URL + "/{id}")
	public ProcessorChainReference get(@Path("sourceId") String sourceId, @Path("id") String id);


	@DELETE(FILTER_URL + "/{id}")
	public Response remove(@Path("sourceId") String sourceId, @Path("id") String id);


	@PUT(FILTER_URL + "/{id}")
	public ProcessorChainReference add(@Path("sourceId") String sourceId, @Path("id") String id, @Body ProcessorChainReference processorChain);

}
