package org.jvalue.ods.rest.v2.api;


import com.google.inject.Inject;
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


	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/{pluginId}")
	public Response addPlugin(
		@RestrictedTo(Role.ADMIN) User user,
		@PathParam("sourceId") String sourceId,
		@PathParam("pluginId") String pluginId,
		@FormDataParam("file") InputStream fileInputStream,
		@FormDataParam("file") FormDataContentDisposition contentDisposition,
		@FormDataParam("file") FormDataBodyPart bodyPart) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		if (pluginManager.contains(source, pluginId))
			throw RestUtils.createJsonFormattedException("plugin with id " + pluginId + " already exists", 409);

		String contentType = bodyPart.getMediaType().toString();
		System.out.println("contentType = " + contentType);
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


	@GET
	public Response getAllPlugins(
		@PathParam("sourceId") String sourceId) {

		List<PluginMetaData> metaDataList = pluginManager.getAll(sourceManager.findBySourceId(sourceId));

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(PluginMetaDataWrapper.fromCollection(metaDataList))
			.addLink("source", getDirectoryURI(uriInfo))
			.build();
	}


	@GET
	@Path("/{pluginId}")
	public Response getPlugin(
		@PathParam("sourceId") String sourceId,
		@PathParam("pluginId") String pluginId,
		@QueryParam("download") boolean download) {

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


	@DELETE
	@Path("/{pluginId}")
	public void deletePlugin(
		@RestrictedTo(Role.ADMIN) User user,
		@PathParam("sourceId") String sourceId,
		@PathParam("pluginId") String pluginId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		pluginManager.remove(source, sourceManager.getDataRepository(source), pluginManager.get(source, pluginId));
	}


}
