package org.jvalue.ods.api.processors;


import org.jvalue.ods.api.sources.DataSourceApi;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface ProcessorApi {

	static String URL_PROCESSOR = DataSourceApi.URL_DATASOURCES + "/{sourceId}/filterChains";

	@GET(URL_PROCESSOR)
	public List<ProcessorReferenceChain> getAll(@Path("sourceId") String sourceId);


	@GET(URL_PROCESSOR + "/{processorId}")
	public ProcessorReferenceChain get(@Path("sourceId") String sourceId, @Path("processorId") String processorId);


	@DELETE(URL_PROCESSOR + "/{processorId}")
	public Response remove(@Path("sourceId") String sourceId, @Path("processorId") String processorId);


	@PUT(URL_PROCESSOR + "/{processorId}")
	public ProcessorReferenceChain add(@Path("sourceId") String sourceId, @Path("processorId") String processorId, @Body ProcessorReferenceChainDescription processorChain);

}
