package org.jvalue.ods.api.notifications;


import org.jvalue.ods.api.sources.DataSourceApi;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface NotificationApi {

	static final String URL_NOTIFICATIONS = DataSourceApi.URL_DATASOURCES + "/{sourceId}/notifications";

	@PUT(URL_NOTIFICATIONS + "/{clientId}")
	public Client register(
			@Path("sourceId") String sourceId,
			@Path("clientId") String clientId,
			@Body ClientDescription clientDescription);


	@GET(URL_NOTIFICATIONS + "/{clientId}")
	public Client get(
			@Path("sourceId") String sourceId,
			@Path("clientId") String clientId);


	@GET(URL_NOTIFICATIONS)
	public List<Client> getAll(
			@Path("sourceId") String sourceId);

	@DELETE(URL_NOTIFICATIONS + "/{clientId}")
	public Response unregister(
			@Path("sourceId") String sourceId,
			@Path("clientId") String clientId);


}
