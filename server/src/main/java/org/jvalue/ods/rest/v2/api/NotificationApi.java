package org.jvalue.ods.rest.v2.api;


import com.google.inject.Inject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.ods.api.notifications.*;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.notifications.NotificationManager;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonLinks;
import org.jvalue.ods.rest.v2.jsonapi.swagger.JsonApiSchema;
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

	@Operation(
		tags = NOTIFICATIONS,
		summary = "Register client",
		description = "Register a client at a datasource for notifications."
	)
	@SecurityRequirement(name = BASICAUTH)
	@ApiResponse(
		responseCode = "201",
		description = "Client registered",
		links = @Link(name = NOTIFICATIONS, operationRef = NOTIFICATIONS)
	)
	@ApiResponse(
		responseCode = "401", description = "Not authorized"
	)
	@ApiResponse(
		responseCode = "404", description = "Source not found"
	)
	@ApiResponse(
		responseCode = "409", description = "Client with given id already exists"
	)
	@POST
	public Response registerClient(
		@RestrictedTo(Role.ADMIN) User user,
		@PathParam("sourceId") String sourceId,
		@Valid @RequestBody(content = @Content(schema = @Schema(implementation = JsonApiSchema.ClientSchema.class)))
			JsonApiRequest clientDescriptionRequest) {


		Client client = requestToClient(clientDescriptionRequest);

		DataSource source = sourceManager.findBySourceId(sourceId);

		//assertIsValidClientDescription(clientDescription, clientDescriptionRequest.getId(), source);

		//Client client = clientDescription.accept(clientAdapter, clientDescriptionRequest.getId());
		notificationManager.add(source, sourceManager.getDataRepository(source), client);

		URI directoryURI = getSanitizedPath(uriInfo);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(ClientWrapper.from(client))
			.addLink(JsonLinks.SELF, directoryURI.resolve(clientDescriptionRequest.getId()))
			.addLink(NOTIFICATIONS, directoryURI)
			.build();
	}


	@Operation(
		tags = NOTIFICATIONS,
		summary = "Unregister client",
		description = "Unregister a client from notifications"
	)
	@ApiResponse(
		responseCode = "200", description = "Client unregistered"
	)
	@ApiResponse(
		responseCode = "401", description = "Not authorized"
	)
	@ApiResponse(
		responseCode = "404", description = "Client or datasource not found"
	)
	@DELETE
	@Path("/{clientId}")
	public void unregisterClient(
		@RestrictedTo(Role.ADMIN) @Parameter(hidden = true)
			User user,
		@PathParam("sourceId") @Parameter(description = "Id of the source from which the client shall be unregistered.")
			String sourceId,
		@PathParam("clientId") @Parameter(description = "Id of the client to unregister")
			String clientId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		Client client = notificationManager.get(source, clientId);
		notificationManager.remove(source, sourceManager.getDataRepository(source), client);
	}


	@Operation(
		tags = NOTIFICATIONS,
		summary = "Get a client",
		description = "Get a notification client from a datasource"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Ok",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.ClientSchema.class)),
		links = @Link(name = NOTIFICATIONS, operationRef = NOTIFICATIONS)
	)
	@ApiResponse(
		responseCode = "404", description = "Client or datasource not found."
	)
	@GET
	@Path("/{clientId}")
	public Response getClient(
		@PathParam("sourceId") @Parameter(description = "Id of the corresponding datasource")
			String sourceId,
		@PathParam("clientId") @Parameter(description = "Id of the client")
			String clientId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		Client client = notificationManager.get(source, clientId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(ClientWrapper.from(client))
			.addLink(NOTIFICATIONS, getDirectoryURI(uriInfo))
			.build();
	}


	@Operation(
		operationId = NOTIFICATIONS,
		tags = NOTIFICATIONS,
		summary = "Get all clients",
		description = "Get all clients registered for notifications at this datasource"
	)
	@ApiResponse(
		responseCode = "200", description = "Ok.",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.ClientSchema.class)),
		links = @Link(name = DATASOURCE, operationRef = DATASOURCE)
	)
	@ApiResponse(
		responseCode = "404", description = "Datasource not found."
	)
	@GET
	public Response getAllClients(
		@PathParam("sourceId") @Parameter(description = "Id of the datasource")
			String sourceId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		List<Client> clients = notificationManager.getAll(source);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(ClientWrapper.fromCollection(clients))
			.addLink(DATASOURCE, getDirectoryURI(uriInfo))
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


		@Override
		public Client visit(NdsClientDescription client, String clientId) {
			return new NdsClient(clientId, client.getHost(), client.getExchange(), client.getValidateMessage());
		}
	}


	private Client requestToClient(JsonApiRequest request) {
		request.getAttributes().put("id", request.getId());
		request.getAttributes().put("type", request.getType());
		return JsonMapper.convertValue(
			request.getAttributes(),
			Client.class);
	}


	private void assertIsValidClientDescription(ClientDescription clientDescription, String id, DataSource source) {
		RequestValidator.validate(clientDescription);

		if (notificationManager.contains(source, id)) {
			throw createJsonApiException("client with id " + id + " already exists", Response.Status.CONFLICT);
		}
	}

}
