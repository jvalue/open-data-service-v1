package org.jvalue.ods.rest.v2.api;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import io.dropwizard.jersey.params.IntParam;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.ods.api.data.Data;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.DataSourceWrapper;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.DataWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static org.jvalue.ods.rest.v2.api.AbstractApi.BASE_URL;
import static org.jvalue.ods.rest.v2.api.AbstractApi.DATA;
import static org.jvalue.ods.utils.HttpUtils.getDirectoryURI;

@Path(BASE_URL + "/{sourceId}/" + DATA)
public final class DataApi extends AbstractApi {

	private static final String DEFAULT_LIMIT = "100";
	private static final int DEFAULT_LIMIT_INT = Integer.parseInt(DEFAULT_LIMIT);
	private static final String DEFAULT_OFFSET = "0";

	private final DataSourceManager sourceManager;


	@Inject
	public DataApi(DataSourceManager sourceManager) {
		this.sourceManager = sourceManager;
	}

	@Context
	private UriInfo uriInfo;


	@GET
	public Response getPaginatedData(
		@PathParam("sourceId") String sourceId,
		@QueryParam("offset") @DefaultValue(DEFAULT_OFFSET) IntParam _offset,
		@QueryParam("limit") @DefaultValue(DEFAULT_LIMIT) IntParam _limit) {

		int offset = _offset.get();
		int limit = _limit.get();

		if (limit < 1 || limit > 100) {
			limit = DEFAULT_LIMIT_INT;
		}

		UriBuilder pathBuilder = uriInfo.getAbsolutePathBuilder();
		if (limit != DEFAULT_LIMIT_INT) {
			pathBuilder.queryParam("limit", _limit);
		}
		URI firstUri = pathBuilder.build();
		URI nextUri = pathBuilder
			.queryParam("offset", offset + limit)
			.build();

		DataRepository repository = getDataRepository(sourceId);
		Data data = repository.executePaginatedGet(String.valueOf(offset), limit);
		List<JsonNode> dataNodes = data.getResult();

		JsonApiResponse.Buildable response = JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataWrapper.fromCollection(dataNodes, sourceManager.findBySourceId(sourceId)))
			.addLink("source", getDirectoryURI(uriInfo));

		System.out.println(dataNodes.size());
		if (dataNodes.size() >= limit) {
			response.addLink("next", nextUri)
				.addLink("first", firstUri);
		}

		return response.build();
	}


	@DELETE
	public void deleteAllObjects(
		@RestrictedTo(Role.ADMIN) User user,
		@PathParam("sourceId") String sourceId) {

		DataRepository repository = getDataRepository(sourceId);
		repository.removeAll();
	}


	@GET
	@Path("/{objectId}")
	public Response getSingleDataObject(
		@PathParam("sourceId") String sourceId,
		@PathParam("objectId") String domainId) {

		JsonNode resultNode = getDataRepository(sourceId).findByDomainId(domainId);
		DataSource source = sourceManager.findBySourceId(sourceId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataWrapper.from(resultNode, source))
			.addRelationship("DataSource", DataSourceWrapper.from(source), getDirectoryURI(uriInfo))
			.addLink(DATA, getDirectoryURI(uriInfo))
			.build();
	}


	@GET
	@Path("/{objectId}/{attribute}")
	public Response getObjectAttribute(
		@PathParam("sourceId") String sourceId,
		@PathParam("objectId") String domainId,
		@PathParam("attribute") String attribute) {

		JsonNode resultNode = getDataRepository(sourceId).findByDomainId(domainId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataWrapper.from(resultNode, sourceManager.findBySourceId(sourceId)))
			.addLink("dataNode", getDirectoryURI(uriInfo))
			.restrictTo(attribute)
			.build();
	}


	private DataRepository getDataRepository(String sourceId) {
		return sourceManager.getDataRepository(sourceManager.findBySourceId(sourceId));
	}

}
