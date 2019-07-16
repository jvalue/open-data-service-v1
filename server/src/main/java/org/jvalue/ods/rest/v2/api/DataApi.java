/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v2.api;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import io.dropwizard.jersey.params.IntParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.ods.api.data.Data;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.swagger.JsonApiSchema;
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

	private static final String NEXT = "next";
	private static final String FIRST = "first";

	private final DataSourceManager sourceManager;


	@Inject
	public DataApi(DataSourceManager sourceManager) {
		this.sourceManager = sourceManager;
	}

	@Context
	private UriInfo uriInfo;

	@Operation(
		operationId = DATA,
		tags = DATA,
		summary = "Get data",
		description = "Get a collection of data, containing $limit data nodes starting at $offset "
	)
	@ApiResponse(
		responseCode = "200",
		description = "The data collection",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.DataCollectionSchema.class)),
		links = {
			@Link(
				name = DATASOURCE,
				operationRef = DATASOURCE,
				description = "Get the source of this data collection"),
			@Link(
				name = NEXT,
				operationRef = DATA,
				description = "Get the next $limit data nodes from this source",
				parameters = {
					@LinkParameter(name = "offset", expression = "$offset + $limit"),
					@LinkParameter(name = "limit", expression = "$limit")
				}),
			@Link(
				name = FIRST,
				operationRef = DATA,
				description = "Get the first $limit data nodes from this source",
				parameters = {
					@LinkParameter(name = "offset", expression = "0"),
					@LinkParameter(name = "limit", expression = "$limit")
				}
			)
		}
	)
	@ApiResponse(
		responseCode = "404", description = "Source not found")
	@GET
	public Response getPaginatedData(
		@PathParam("sourceId")
		@Parameter(description = "Id of the source of the data")
			String sourceId,
		@QueryParam("offset") @DefaultValue(DEFAULT_OFFSET)
		@Parameter(description = "The start id for the fetched collection", schema = @Schema(implementation = Integer.class))
			IntParam _offset,
		@QueryParam("limit") @DefaultValue(DEFAULT_LIMIT)
		@Parameter(description = "Amount of data nodes that will be returned" , schema = @Schema(implementation = Integer.class))
			IntParam _limit) {

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
			.addLink(DATASOURCE, getDirectoryURI(uriInfo));

		if (dataNodes.size() >= limit) {
			response.addLink("next", nextUri)
				.addLink("first", firstUri);
		}
		return response.build();
	}


	@Operation(
		summary = "Delete data",
		description = "Delete all data objects provided by a source",
		tags = DATA
	)
	@SecurityRequirement(name = BASICAUTH)
	@ApiResponse(
		responseCode = "200", description = "Deleted"
	)
	@ApiResponse(
		responseCode = "401", description = "Not authorized"
	)
	@ApiResponse(
		responseCode = "404", description = "Datasource not found"
	)
	@DELETE
	public void deleteAllObjects(
		@RestrictedTo(Role.ADMIN) @Parameter(hidden = true) User user,
		@PathParam("sourceId") @Parameter(description = "Id of the source") String sourceId) {

		DataRepository repository = getDataRepository(sourceId);
		repository.removeAll();
	}


	@Operation(
		tags = DATA,
		summary = "Get data object",
		description = "Get a single data node")
	@ApiResponse(
		responseCode = "200", description = "Ok",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.DataSchema.class)))
	@ApiResponse(
		responseCode = "404", description = "Source not found or data node not found")
	@GET
	@Path("/{objectId}")
	public Response getSingleDataObject(
		@PathParam("sourceId") @Parameter(description = "Id of the data object's source") String sourceId,
		@PathParam("objectId") @Parameter(description = "Domain id of the data object") String domainId) {

		JsonNode resultNode = getDataRepository(sourceId).findByDomainId(domainId);
		DataSource source = sourceManager.findBySourceId(sourceId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataWrapper.from(resultNode, source))
			.addRelationship("DataSource", DataSourceWrapper.from(source), getDirectoryURI(uriInfo))
			.addLink(DATA, getDirectoryURI(uriInfo))
			.build();
	}


	@Operation(
		summary = "Get attribute",
		tags = DATA,
		description = "Get one attribute of a data object, i.e. get the data object, restricted to type, id and the attribute"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Ok",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.DataSchema.class))
	)
	@ApiResponse(
		responseCode = "404",
		description = "Data node not found"
	)
	@GET
	@Path("/{objectId}/{attribute}")
	public Response getObjectAttribute(
		@PathParam("sourceId") @Parameter(description = "The source of the data object") String sourceId,
		@PathParam("objectId") @Parameter(description = "The domain id of the data object") String domainId,
		@PathParam("attribute") @Parameter(description = "The attribute to be fetched") String attribute) {

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
