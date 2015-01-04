package org.jvalue.ods.api.processors;


import org.jvalue.ods.api.notifications.NotificationApi;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface ProcessorApi {

	static String URL_PROCESSOR = NotificationApi.URL_NOTIFICATIONS + "/{sourceId}/filterChains";

	@GET(URL_PROCESSOR)
	public List<ProcessorChain> getAll(@Path("sourceId") String sourceId);


	@GET(URL_PROCESSOR + "/{processorId}")
	public ProcessorChain get(@Path("sourceId") String sourceId, @Path("processorId") String processorId);


	@DELETE(URL_PROCESSOR + "/{processorId}")
	public Response remove(@Path("sourceId") String sourceId, @Path("processorId") String processorId);


	@PUT(URL_PROCESSOR + "/{processorId}")
	public ProcessorChain add(@Path("sourceId") String sourceId, @Path("processorId") String processorId, @Body ProcessorChainDescription processorChain);

}
