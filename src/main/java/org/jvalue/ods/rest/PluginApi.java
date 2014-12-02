package org.jvalue.ods.rest;


import com.google.inject.Inject;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.filter.plugin.PluginMetaData;
import org.jvalue.ods.filter.plugin.PluginMetaDataManager;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path(AbstractApi.BASE_URL + "/{sourceId}/plugins")
@Produces(MediaType.APPLICATION_JSON)
public final class PluginApi extends AbstractApi {

	private static final String
			TYPE_X_JAVA_ARCHIVE = "application/x-java-archive",
			TYPE_JAVA_ARCHIVE = "application/java-archive";

	private final DataSourceManager sourceManager;
	private final PluginMetaDataManager pluginManager;


	@Inject
	PluginApi(
			PluginMetaDataManager pluginManager,
			DataSourceManager sourceManager) {

		this.pluginManager = pluginManager;
		this.sourceManager = sourceManager;
	}


	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/{pluginId}")
	public PluginMetaData uploadFile(
			@PathParam("sourceId") String sourceId,
			@PathParam("pluginId") String pluginId,
			@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDisposition,
			@FormDataParam("file") FormDataBodyPart bodyPart) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		if (pluginManager.contains(source, pluginId)) throw RestUtils.createJsonFormattedException("plugin with id " + pluginId + " already exists", 409);

		String contentType = bodyPart.getMediaType().toString();
		System.out.println("contentType = " + contentType);
		if (!contentType.equals(TYPE_X_JAVA_ARCHIVE) && !contentType.equals(TYPE_JAVA_ARCHIVE))
			throw RestUtils.createJsonFormattedException("content type must be " + TYPE_X_JAVA_ARCHIVE + " or " + TYPE_JAVA_ARCHIVE, 415);

		PluginMetaData metaData = new PluginMetaData(pluginId);
		pluginManager.addFile(source, metaData, fileInputStream, contentType);
		return metaData;
	}


	@GET
	public List<PluginMetaData> getAll(
		@PathParam("sourceId") String sourceId) {

		return pluginManager.getAll(sourceManager.findBySourceId(sourceId));
	}


	@GET
	@Path("/{pluginId}")
	public PluginMetaData getSingle(
			@PathParam("sourceId") String sourceId,
			@PathParam("pluginId") String pluginId) {

		return pluginManager.get(sourceManager.findBySourceId(sourceId), pluginId);
	}

}
