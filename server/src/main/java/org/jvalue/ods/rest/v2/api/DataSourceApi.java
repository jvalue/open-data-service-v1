/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
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
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.data.DataViewManager;
import org.jvalue.ods.notifications.NotificationManager;
import org.jvalue.ods.processor.ProcessorChainManager;
import org.jvalue.ods.processor.plugin.PluginMetaDataManager;
import org.jvalue.ods.rest.v2.jsonapi.request.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonLinks;
import org.jvalue.ods.rest.v2.jsonapi.swagger.JsonApiSchema;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.*;
import org.jvalue.ods.utils.JsonMapper;
import org.jvalue.ods.utils.RequestValidator;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static org.jvalue.ods.utils.HttpUtils.getDirectoryURI;
import static org.jvalue.ods.utils.HttpUtils.getSanitizedPath;


@Path(AbstractApi.BASE_URL)
public final class DataSourceApi extends AbstractApi {

	private final DataSourceManager sourceManager;
	private final ProcessorChainManager chainManager;
	private final PluginMetaDataManager pluginManager;
	private final NotificationManager notificationManager;
	private final DataViewManager viewManager;
	@Context
	private UriInfo uriInfo;

	@Inject
	public DataSourceApi(DataSourceManager sourceManager,
						 ProcessorChainManager chainManager,
						 PluginMetaDataManager pluginManager,
						 NotificationManager notificationManager,
						 DataViewManager viewManager) {
		this.sourceManager = sourceManager;
		this.chainManager = chainManager;
		this.pluginManager = pluginManager;
		this.notificationManager = notificationManager;
		this.viewManager = viewManager;
	}

	@Operation(
		tags = DATASOURCES,
		operationId = DATASOURCES,
		summary = "Get all datasources",
		description = "Returns a list of all datasources registered at the ODS")
	@ApiResponse(
		responseCode = "200",
		description = "Ok",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.DataSourceSchema.class)),
		links = @Link(name = ENTRYPOINT, operationRef = ENTRYPOINT, description = "Go to api entrypoint"))
	@GET
	public Response getAllSources() {
		List<DataSource> sources = sourceManager.getAll();

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataSourceWrapper.fromCollection(sources))
			.addLink(ENTRYPOINT, getDirectoryURI(uriInfo))
			.build();
	}


	@Operation(
		tags = DATASOURCES,
		operationId = DATASOURCE,
		summary = "Get a datasource",
		description = "Get datasource with a specific sourceId")
	@ApiResponse(
		responseCode = "200",
		description = "Ok",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.DataSourceSchema.class)),
		links = {
			@Link(name = VIEWS, operationRef = VIEWS, description = "Get views for this datasource"),
			@Link(name = NOTIFICATIONS, operationRef = NOTIFICATIONS, description = "Get notification clients for this datasource"),
			@Link(name = PLUGINS, operationRef = PLUGINS, description = "Get plugins for this datasource")
		})
	@ApiResponse(responseCode = "404", description = "Datasource not found")
	@GET
	@Path("/{sourceId}")
	public Response getSource(
		@PathParam("sourceId") @Parameter(description = "The id of the datasource")
			String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);

		JsonApiResponse.Buildable response = JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataSourceWrapper.from(source))
			.addLink(DATA, getSanitizedPath(uriInfo).resolve(DATA))
			.addLink(DATASOURCES, getDirectoryURI(uriInfo));

		response = addDatasourceRelationships(response, source);

		return response.build();
	}


	@Operation(
		tags = DATASOURCES,
		summary = "Get the schema of a specific datasource",
		description = "Get a view of the datasource with sourceId which is restricted to id, type and schema attributes")
	@ApiResponse(responseCode = "200", description = "Ok")
	@ApiResponse(responseCode = "404", description = "Datasource not found")
	@GET
	@Path("/{sourceId}/schema")
	public Response getSourceSchema(
		@PathParam("sourceId") @Parameter(description = "Id of the datasource") String sourceId) {

		DataSource source = sourceManager.findBySourceId(sourceId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataSourceWrapper.from(source))
			.addLink(DATASOURCE, getDirectoryURI(uriInfo))
			.restrictTo("schema")
			.build();
	}


	@Operation(
		tags = DATASOURCES,
		summary = "Add a datasource",
		description = "add a datasource to the open-data-service")
	@SecurityRequirement(name = BASICAUTH)
	@ApiResponse(responseCode = "201", description = "Datasource added")
	@ApiResponse(responseCode = "401", description = "Not authorized")
	@ApiResponse(responseCode = "409", description = "Source with sourceId already existing")
	@POST
	public Response addSource(
		@RestrictedTo(Role.ADMIN) @Parameter(hidden = true) User user,
		@PathParam("sourceId") @Parameter(description = "The id of the source to be added") String sourceId,
		@RequestBody
			(description = "Description of the datasource to be added",
			required = true,
			content = @Content(schema = @Schema(implementation = JsonApiSchema.DataSourceSchema.class)))
			JsonApiRequest sourceDescriptionRequest) {

		DataSourceDescription sourceDescription = JsonMapper.convertValue(
			sourceDescriptionRequest.getAttributes(),
			DataSourceDescription.class);

		assertIsValidSourceDescription(sourceDescription);
		assertSourceDoesNotExist(sourceDescriptionRequest.getId());

		DataSource source = new DataSource(
			sourceDescriptionRequest.getId(),
			sourceDescription.getDomainIdKey(),
			sourceDescription.getSchema(),
			sourceDescription.getMetaData()
		);
		sourceManager.add(source);

		URI directoryURI = getSanitizedPath(uriInfo);

		return JsonApiResponse
			.createPostResponse(uriInfo)
			.data(DataSourceWrapper.from(source))
			.addLink(JsonLinks.SELF, directoryURI.resolve(sourceDescriptionRequest.getId()))
			.addLink("sources", directoryURI)
			.addLink(DATASOURCES, getDirectoryURI(uriInfo))
			.build();
	}


	@Operation(
		tags = DATASOURCES,
		summary = "Delete a datasource",
		description = "Delete a datasource from the open-data-service")
	@SecurityRequirement(name = BASICAUTH)
	@ApiResponse(responseCode = "200", description = "Source deleted")
	@ApiResponse(responseCode = "401", description = "Not authorized")
	@ApiResponse(responseCode = "404", description = "Source not found")
	@DELETE
	@Path("/{sourceId}")
	public Response deleteSource(
		@RestrictedTo(Role.ADMIN) @Parameter(hidden = true) User user,
		@PathParam("sourceId") @Parameter(description = "The id of the source to be deleted")
			String sourceId) {

		sourceManager.remove(sourceManager.findBySourceId(sourceId));

		return Response
			.ok()
			.build();
	}


	private JsonApiResponse.Buildable addDatasourceRelationships(JsonApiResponse.Buildable response,
																 DataSource source) {

		List<ProcessorReferenceChain> chains = chainManager.getAll(source);
		List<PluginMetaData> plugins = pluginManager.getAll(source);
		List<Client> notifications = notificationManager.getAll(source);
		List<DataView> views = viewManager.getAll(source);

		URI path = getSanitizedPath(uriInfo);

		if (!chains.isEmpty()) {
			response.addRelationship(FILTERCHAINS, ProcessorReferenceChainWrapper.fromCollection(chains), path.resolve(FILTERCHAINS));
		}
		if (!plugins.isEmpty()) {
			response.addRelationship(PLUGINS, PluginMetaDataWrapper.fromCollection(plugins), path.resolve(PLUGINS));
		}
		if (!notifications.isEmpty()) {
			response.addRelationship(NOTIFICATIONS, ClientWrapper.fromCollection(notifications), path.resolve(NOTIFICATIONS));
		}
		if (!views.isEmpty()) {
			response.addRelationship(VIEWS, DataViewWrapper.fromCollection(views), path.resolve(VIEWS));
		}

		return response;
	}


	private void assertIsValidSourceDescription(DataSourceDescription sourceDescription) {
		RequestValidator.validate(sourceDescription);
	}


	private void assertSourceDoesNotExist(String sourceId) {
		if (sourceManager.isValidSourceId(sourceId)) {
			throw createJsonApiException("Source with id " + sourceId + " already exists!", Response.Status.CONFLICT);
		}
	}
}
