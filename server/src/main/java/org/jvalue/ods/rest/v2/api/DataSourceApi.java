package org.jvalue.ods.rest.v2.api;


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
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.data.DataViewManager;
import org.jvalue.ods.notifications.NotificationManager;
import org.jvalue.ods.processor.ProcessorChainManager;
import org.jvalue.ods.processor.plugin.PluginMetaDataManager;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.*;
import org.jvalue.ods.utils.JsonMapper;
import org.jvalue.ods.utils.RequestValidator;

import javax.validation.ConstraintViolation;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Set;

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

	@GET
	public Response getAllSources() {
		List<DataSource> sources = sourceManager.getAll();

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataSourceWrapper.fromCollection(sources))
			.addLink(ENTRYPOINT, getDirectoryURI(uriInfo))
			.build();
	}


	@GET
	@Path("/{sourceId}")
	public Response getSource(@PathParam("sourceId") String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);

		JsonApiResponse.Buildable response = JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataSourceWrapper.from(source))
			.addLink(DATA, getSanitizedPath(uriInfo).resolve(DATA))
			.addLink("sources", getDirectoryURI(uriInfo));

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
			.addLink("source", getDirectoryURI(uriInfo))
			.restrictTo("schema")
			.build();
	}


	@POST
	public Response addSource(
		@RestrictedTo(Role.ADMIN) User user,
		JsonApiRequest sourceDescriptionRequest) {

		DataSourceDescription sourceDescription = JsonMapper.convertValue(
			sourceDescriptionRequest.getAttributes(),
			DataSourceDescription.class
		);

		assertIsValidSourceDescription(sourceDescription, sourceDescriptionRequest.getId());

		DataSource source = new DataSource(
			sourceDescriptionRequest.getId(),
			sourceDescription.getDomainIdKey(),
			sourceDescription.getSchema(),
			sourceDescription.getMetaData()
		);
		sourceManager.add(source);


		return JsonApiResponse
			.createPostResponse(uriInfo)
			.data(DataSourceWrapper.from(source))
			.addLink("sources", getDirectoryURI(uriInfo))
			.build();
	}


	@DELETE
	@Path("/{sourceId}")
	public Response deleteSource(
		@RestrictedTo(Role.ADMIN) User user,
		@PathParam("sourceId") String sourceId) {

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


	private void assertIsValidSourceDescription(DataSourceDescription sourceDescription, String id) {

		Set<ConstraintViolation<DataSourceDescription>> violations = RequestValidator.validate(sourceDescription);
		if (!violations.isEmpty()) {
			StringBuilder violationStringBuilder = new StringBuilder();
			for (ConstraintViolation<DataSourceDescription> violation : violations) {
				violationStringBuilder.append(violation.getPropertyPath().toString())
					.append(" ")
					.append(violation.getMessage())
					.append(System.lineSeparator());
			}
			throw RestUtils.createJsonFormattedException("Malformed DataSourceDescription: " + violationStringBuilder.toString(), 400);
		}

		if (sourceManager.isValidSourceId(id)) {
			throw RestUtils.createJsonFormattedException("source with id " + id + " already exists", 409);
		}
	}
}
