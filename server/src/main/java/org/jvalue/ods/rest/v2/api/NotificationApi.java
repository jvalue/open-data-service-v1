package org.jvalue.ods.rest.v2.api;


import com.google.inject.Inject;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.ods.api.notifications.*;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.notifications.NotificationManager;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonLinks;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.ClientWrapper;
import org.jvalue.ods.utils.JsonMapper;
import org.jvalue.ods.utils.RequestValidator;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static org.jvalue.ods.rest.v2.api.AbstractApi.BASE_URL;
import static org.jvalue.ods.rest.v2.api.AbstractApi.NOTIFICATIONS;
import static org.jvalue.ods.utils.HttpUtils.getDirectoryURI;
import static org.jvalue.ods.utils.HttpUtils.getSanitizedPath;

@Path(BASE_URL + "/{sourceId}/" + NOTIFICATIONS)
public final class NotificationApi extends AbstractApi {

	private final DataSourceManager sourceManager;
	private final NotificationManager notificationManager;
	private final ClientAdapter clientAdapter = new ClientAdapter();

	@Context
	private UriInfo uriInfo;

	@Inject
	NotificationApi(
		DataSourceManager sourceManager,
		NotificationManager notificationManager) {

		this.sourceManager = sourceManager;
		this.notificationManager = notificationManager;
	}


	@POST
	public Response registerClient(
		@RestrictedTo(Role.ADMIN) User user,
		@PathParam("sourceId") String sourceId,
		@Valid JsonApiRequest clientDescriptionRequest) {

		ClientDescription clientDescription = JsonMapper.convertValue(
			clientDescriptionRequest.getAttributes(),
			ClientDescription.class
		);

		DataSource source = sourceManager.findBySourceId(sourceId);

		assertIsValidClientDescription(clientDescription, clientDescriptionRequest.getId(), source);

		Client client = clientDescription.accept(clientAdapter, clientDescriptionRequest.getId());
		notificationManager.add(source, sourceManager.getDataRepository(source), client);

		URI directoryURI = getSanitizedPath(uriInfo);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(ClientWrapper.from(client))
			.addLink(JsonLinks.SELF, directoryURI.resolve(clientDescriptionRequest.getId()))
			.addLink(NOTIFICATIONS, directoryURI)
			.build();
	}

	@DELETE
	@Path("/{clientId}")
	public void unregisterClient(
		@RestrictedTo(Role.ADMIN) User user,
		@PathParam("sourceId") String sourceId,
		@PathParam("clientId") String clientId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		Client client = notificationManager.get(source, clientId);
		notificationManager.remove(source, sourceManager.getDataRepository(source), client);
	}


	@GET
	@Path("/{clientId}")
	public Response getClient(
		@PathParam("sourceId") String sourceId,
		@PathParam("clientId") String clientId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		Client client = notificationManager.get(source, clientId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(ClientWrapper.from(client))
			.addLink(NOTIFICATIONS, getDirectoryURI(uriInfo))
			.build();
	}


	@GET
	public Response getAllClients(
		@PathParam("sourceId") String sourceId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		List<Client> clients = notificationManager.getAll(source);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(ClientWrapper.fromCollection(clients))
			.addLink("source", getDirectoryURI(uriInfo))
			.build();
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

		@Override
		public Client visit(AmqpClientDescription client, String clientId) {
			return new AmqpClient(clientId, client.getHost(), client.getExchange(), client.getExchangeType(), client.getRoutingKey());
		}

	}


	private void assertIsValidClientDescription(ClientDescription clientDescription, String id, DataSource source) {
		RequestValidator.validate(clientDescription);

		if (notificationManager.contains(source, id)) {
			throw RestUtils.createJsonFormattedException("client with id " + id + " already exists", 409);
		}
	}

}
