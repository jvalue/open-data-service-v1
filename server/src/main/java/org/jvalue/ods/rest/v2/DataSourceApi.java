package org.jvalue.ods.rest.v2;


import com.google.inject.Inject;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.data.AbstractDataSourcePropertyManager;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.data.DataViewManager;
import org.jvalue.ods.notifications.NotificationManager;
import org.jvalue.ods.processor.ProcessorChainManager;
import org.jvalue.ods.processor.plugin.PluginMetaDataManager;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.*;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path(AbstractApi.BASE_URL)
public final class DataSourceApi extends AbstractApi {

	private final DataSourceManager sourceManager;
	private final ProcessorChainManager chainManager;
	private final PluginMetaDataManager pluginManager;
	private final NotificationManager notificationManager;
	private final DataViewManager viewManager;
	private final AbstractDataSourcePropertyManager[] managers;
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
		this.managers = new AbstractDataSourcePropertyManager[]{
			chainManager, pluginManager, notificationManager, viewManager
		};

	}

	@GET
	public Response getAllSources() {
		List<DataSource> sources = sourceManager.getAll();

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataSourceWrapper.fromCollection(sources))
			.build();
	}


	@GET
	@Path("/{sourceId}")
	public Response getSource(@PathParam("sourceId") String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);

		JsonApiResponse.Buildable response = JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataSourceWrapper.from(source));

		response = addDatasourceRelationships(response, source);

		return response.build();
	}


	@GET
	@Path("/{sourceId}/schema")
	public Response getSourceSchema(@PathParam("sourceId") String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataSourceWrapper.from(source))
			.restrictTo("schema")
			.build();
	}


	@POST
	@Path("/{sourceId}")
	public Response addSource(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId,
			@Valid DataSourceDescription sourceDescription) {

		if (sourceManager.isValidSourceId(sourceId))
			throw RestUtils.createJsonFormattedException("source with id " + sourceId + " already exists", 409);

		DataSource source = new DataSource(
			sourceId,
			sourceDescription.getDomainIdKey(),
			sourceDescription.getSchema(),
			sourceDescription.getMetaData());
		sourceManager.add(source);

		return JsonApiResponse
			.createPostResponse(uriInfo)
			.data(DataSourceWrapper.from(source))
			.build();
	}


	@DELETE
	@Path("/{sourceId}")
	public Response deleteSource(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId) {

		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}


	private JsonApiResponse.Buildable addDatasourceRelationships(JsonApiResponse.Buildable response, DataSource source) {
		List<ProcessorReferenceChain> chains = chainManager.getAll(source);
		List<PluginMetaData> plugins = pluginManager.getAll(source);
		List<Client> notifications = notificationManager.getAll(source);
		List<DataView> views = viewManager.getAll(source);

		URI path = uriInfo.getAbsolutePath();

		if(!chains.isEmpty()) {
			response.addRelationship("FilterChains", ProcessorReferenceChainWrapper.fromCollection(chains), path.resolve("./filterChains"));
		}
		if(!plugins.isEmpty()) {
			response.addRelationship("Plugins", PluginMetaDataWrapper.fromCollection(plugins), path.resolve("./plugins"));
		}
		if(!notifications.isEmpty()) {
			response.addRelationship("Notifications", ClientWrapper.fromCollection(notifications), path.resolve("./notifications"));
		}
		if(!views.isEmpty()) {
			response.addRelationship("Views", DataViewWrapper.fromCollection(views), path.resolve("./views"));
		}

		return response;
	}

}
