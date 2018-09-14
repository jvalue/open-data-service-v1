package org.jvalue.ods.rest.v2.api;


import com.google.inject.Inject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.processor.plugin.PluginMetaDataManager;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.swagger.JsonApiSchema;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.PluginMetaDataWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;
import java.util.List;

import static org.jvalue.ods.rest.v2.api.AbstractApi.BASE_URL;
import static org.jvalue.ods.rest.v2.api.AbstractApi.PLUGINS;
import static org.jvalue.ods.utils.HttpUtils.getDirectoryURI;


@Path(BASE_URL + "/{sourceId}/" + PLUGINS)
public final class PluginApi extends AbstractApi {

	private static final String
		TYPE_X_JAVA_ARCHIVE = "application/x-java-archive",
		TYPE_JAVA_ARCHIVE = "application/java-archive";

	private final DataSourceManager sourceManager;
	private final PluginMetaDataManager pluginManager;

	@Context
	private UriInfo uriInfo;

	@Inject
	PluginApi(
		PluginMetaDataManager pluginManager,
		DataSourceManager sourceManager) {

		this.pluginManager = pluginManager;
		this.sourceManager = sourceManager;
	}


	@Operation(
		tags = PLUGINS,
		summary = "Add plugin",
		description = "Add a plugin to a datasource"
	)
	@ApiResponse(
		responseCode = "201",
		description = "Plugin added",
		links = @Link(name = PLUGINS, operationRef = PLUGINS),
		content = @Content(schema = @Schema(implementation = JsonApiSchema.PluginMetaDataSchema.class))
	)
	@ApiResponse(
		responseCode = "401", description = "Not authorized"
	)
	@ApiResponse(
		responseCode = "404", description = "Datasource not found"
	)
	@ApiResponse(
		responseCode = "409", description = "Plugin with given id already exists"
	)
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/{pluginId}")
	public Response addPlugin(
		@RestrictedTo(Role.ADMIN) @Parameter(hidden = true)
			User user,
		@PathParam("sourceId") @Parameter(description = "Id of the datasource to which the plugin shall be added")
			String sourceId,
		@PathParam("pluginId") @Parameter(description = "Id of the plugin")
			String pluginId,
		@Parameter(hidden = true) @FormDataParam("file")
			InputStream fileInputStream,
		@Parameter(hidden = true) @FormDataParam("file")
			FormDataContentDisposition contentDisposition,
		@Parameter(hidden = true) @FormDataParam("file")
			FormDataBodyPart bodyPart) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		if (pluginManager.contains(source, pluginId))
			throw RestUtils.createJsonFormattedException("plugin with id " + pluginId + " already exists", 409);

		String contentType = bodyPart.getMediaType().toString();
		if (!contentType.equals(TYPE_X_JAVA_ARCHIVE) && !contentType.equals(TYPE_JAVA_ARCHIVE))
			throw RestUtils.createJsonFormattedException("content type must be " + TYPE_X_JAVA_ARCHIVE + " or " + TYPE_JAVA_ARCHIVE, 415);

		PluginMetaData metaData = new PluginMetaData(pluginId, "someDummyAuthorForNow");
		pluginManager.addFile(source, metaData, fileInputStream, contentType);

		return JsonApiResponse
			.createPostResponse(uriInfo)
			.data(PluginMetaDataWrapper.from(metaData))
			.addLink(PLUGINS, getDirectoryURI(uriInfo))
			.build();
	}


	@Operation(
		operationId = PLUGINS,
		tags = PLUGINS,
		summary = "Get all plugins",
		description = "Get all plugins for a datasource"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Ok",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.PluginMetaDataSchema.class))
	)
	@ApiResponse(
		responseCode = "404", description = "Datasource not found."
	)
	@GET
	public Response getAllPlugins(
		@PathParam("sourceId") @Parameter(description = "Id of the datasource")
			String sourceId) {

		List<PluginMetaData> metaDataList = pluginManager.getAll(sourceManager.findBySourceId(sourceId));

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(PluginMetaDataWrapper.fromCollection(metaDataList))
			.addLink(DATASOURCE, getDirectoryURI(uriInfo))
			.build();
	}


	@Operation(
		tags = PLUGINS,
		summary = "Get a plugin",
		description = "Get a plugin by its id"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Ok",
		links = @Link(name = PLUGINS, operationRef = PLUGINS),
		content = @Content(schema = @Schema(implementation = JsonApiSchema.PluginMetaDataSchema.class))
	)
	@GET
	@Path("/{pluginId}")
	public Response getPlugin(
		@PathParam("sourceId") @Parameter(description = "Id of the datasource")
			String sourceId,
		@PathParam("pluginId") @Parameter(description = "Id of the plugin")
			String pluginId,
		@QueryParam("download") @DefaultValue("false") @Parameter(description = "Download of plugins is not supported by api v2")
			boolean download) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		PluginMetaData metaData = pluginManager.get(source, pluginId);
		if (!download) {
			return JsonApiResponse
				.createGetResponse(uriInfo)
				.data(PluginMetaDataWrapper.from(metaData))
				.addLink(PLUGINS, getDirectoryURI(uriInfo))
				.build();
		} else {
			return Response
				.status(Response.Status.NOT_IMPLEMENTED)
				.build();
		}
	}


	@Operation(
		tags = PLUGINS,
		summary = "Delete a plugin",
		description = "Delete a plugin from a datasource"
	)
	@ApiResponse(
		responseCode = "200", description = "Plugin deleted"
	)
	@ApiResponse(
		responseCode = "404", description = "Plugin or datasource not found"
	)
	@DELETE
	@Path("/{pluginId}")
	public void deletePlugin(
		@RestrictedTo(Role.ADMIN) @Parameter(hidden = true) User user,
		@PathParam("sourceId") @Parameter(description = "Id of the source from which the plugin shall be deleted")
			String sourceId,
		@PathParam("pluginId") @Parameter(description = "If of the plugin to be deleted")
			String pluginId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		pluginManager.remove(source, sourceManager.getDataRepository(source), pluginManager.get(source, pluginId));
	}


}
