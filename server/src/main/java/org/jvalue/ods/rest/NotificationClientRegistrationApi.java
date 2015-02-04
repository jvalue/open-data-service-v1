package org.jvalue.ods.rest;


import com.google.inject.Inject;

import org.jvalue.common.rest.RestUtils;
import org.jvalue.ods.api.notifications.ClientDescription;
import org.jvalue.ods.api.notifications.ClientDescriptionVisitor;
import org.jvalue.ods.api.notifications.GcmClientDescription;
import org.jvalue.ods.api.notifications.HttpClientDescription;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.notifications.NotificationManager;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.api.notifications.HttpClient;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AbstractApi.BASE_URL + "/{sourceId}/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class NotificationClientRegistrationApi extends AbstractApi {

	private final DataSourceManager sourceManager;
	private final NotificationManager notificationManager;
	private final ClientAdapter clientAdapter = new ClientAdapter();

	@Inject
	NotificationClientRegistrationApi(
			DataSourceManager sourceManager,
			NotificationManager notificationManager) {

		this.sourceManager = sourceManager;
		this.notificationManager = notificationManager;
	}


	@PUT
	@Path("/{clientId}")
	public Client registerClient(
			@PathParam("sourceId") String sourceId,
			@PathParam("clientId") String clientId,
			@Valid ClientDescription clientDescription) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		if (notificationManager.contains(source, clientId)) throw RestUtils.createJsonFormattedException("client with id " + clientId + " already exists", 409);
		Client client = clientDescription.accept(clientAdapter, clientId);
		notificationManager.add(source, sourceManager.getDataRepository(source), client);
		return client;
	}


	@DELETE
	@Path("/{clientId}")
	public void unregisterClient(
			@PathParam("sourceId") String sourceId,
			@PathParam("clientId") String clientId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		Client client = notificationManager.get(source, clientId);
		notificationManager.remove(source, sourceManager.getDataRepository(source), client);
	}


	@GET
	@Path("/{clientId}")
	public Client getSingleClient(
			@PathParam("sourceId") String sourceId,
			@PathParam("clientId") String clientId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		return notificationManager.get(source, clientId);
	}


	@GET
	public List<Client> getAllClients(
			@PathParam("sourceId") String sourceId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		return notificationManager.getAll(source);
	}


	private static final class ClientAdapter implements ClientDescriptionVisitor<String, Client> {

		@Override
		public Client visit(GcmClientDescription client, String clientId) {
			return new GcmClient(clientId, client.getGcmClientId());
		}


		@Override
		public Client visit(HttpClientDescription client, String clientId) {
			return new HttpClient(clientId, client.getCallbackUrl(), client.getSendData());
		}

	}

}
