package org.jvalue.ods.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.inject.Inject;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.notifications.NotificationManager;
import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.notifications.clients.HttpClient;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
		Client client = clientDescription.toClient(clientId);
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


	@JsonTypeInfo(
			use = JsonTypeInfo.Id.NAME,
			include = JsonTypeInfo.As.PROPERTY,
			property = "type",
			visible = true
	)
	@JsonSubTypes({
			@JsonSubTypes.Type(value = HttpClientDescription.class, name = HttpClient.CLIENT_TYPE),
			@JsonSubTypes.Type(value = GcmClientDescription.class, name = GcmClient.CLIENT_TYPE)
	})
	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	private static abstract class ClientDescription {
		@NotNull protected String type;

		public abstract Client toClient(String clientId);
	}


	private static final class HttpClientDescription extends ClientDescription {
		@NotNull private String restUrl;
		@NotNull private String sourceParam;
		@NotNull private boolean sendData;

		@Override
		public Client toClient(String clientId) {
			return new HttpClient(clientId, restUrl, sourceParam, sendData);
		}

	}


	private static final class GcmClientDescription extends ClientDescription {
		@NotNull private String gcmClientId;

		@Override
		public Client toClient(String clientId) {
			return new GcmClient(clientId, gcmClientId);
		}

	}

}
