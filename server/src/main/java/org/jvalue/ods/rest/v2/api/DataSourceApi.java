package org.jvalue.ods.rest.v2.api;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.commons.utils.Assert;
import org.jvalue.commons.utils.Log;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.data.DataViewManager;
import org.jvalue.ods.notifications.NotificationManager;
import org.jvalue.ods.processor.ProcessorChainManager;
import org.jvalue.ods.processor.plugin.PluginMetaDataManager;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static org.jvalue.ods.utils.HttpUtils.getDirectoryURI;
import static org.jvalue.ods.utils.HttpUtils.getSanitizedPath;
import static org.jvalue.ods.utils.JsonUtils.assertIsValidJsonApiSingleDataDocument;


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
		JsonNode sourceDescription) {

		try {
			assertIsValidSourceDescription(sourceDescription);
		} catch (IllegalArgumentException e) {
			Log.error(e.toString() + "/n stacktrace: " + ExceptionUtils.getStackTrace(e));
			throw RestUtils.createJsonFormattedException("SourceDescription has wrong format.", 400);
		}

		DataSource source = createSource(sourceDescription);

		if (sourceManager.isValidSourceId(source.getId())) {
			throw RestUtils.createJsonFormattedException("source with id " + source.getId() + " already exists", 409);
		}

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


	private DataSource createSource(JsonNode sourceDescription) {

		String sourceId = sourceDescription.get("data").get("id").asText();

		JsonNode attributeNode = sourceDescription.get("data").get("attributes");
		String domainIdKey = attributeNode.get("domainIdKey").asText();
		JsonNode schema = attributeNode.get("schema");

		DataSourceMetaData metaData;

		if(attributeNode.has("metaData")) {
			JsonNode metaDataNode = attributeNode.get("metaData");
			metaData = new DataSourceMetaData(
				metaDataNode.get("name").asText(),
				metaDataNode.get("title").asText(),
				metaDataNode.get("author").asText(),
				metaDataNode.get("authorEmail").asText(),
				metaDataNode.get("notes").asText(),
				metaDataNode.get("url").asText(),
				metaDataNode.get("termsOfUse").asText()
			);
		} else {
			metaData = new DataSourceMetaData("","","","","","","");
		}

		return new DataSource(
			sourceId,
			JsonPointer.compile(domainIdKey),
			schema,
			metaData
		);
	}


	private static void assertIsValidSourceDescription(JsonNode sourceDescription) {

		assertIsValidJsonApiSingleDataDocument(sourceDescription);

		Assert.assertNotNull(sourceDescription);
		Assert.assertTrue(sourceDescription.get("data").has("attributes"));

		JsonNode attributeNode = sourceDescription.get("data").get("attributes");
		Assert.assertTrue(attributeNode.has("domainIdKey"));

		JsonNode metaDataNode = attributeNode.get("metaData");
		Assert.assertTrue(metaDataNode.has("name"));
		Assert.assertTrue(metaDataNode.has("title"));
		Assert.assertTrue(metaDataNode.has("author"));
		Assert.assertTrue(metaDataNode.has("authorEmail"));
		Assert.assertTrue(metaDataNode.has("notes"));
		Assert.assertTrue(metaDataNode.has("url"));
		Assert.assertTrue(metaDataNode.has("termsOfUse"));
	}
}
