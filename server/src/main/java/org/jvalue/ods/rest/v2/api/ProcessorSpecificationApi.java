/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v2.api;


import com.google.inject.Inject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.jvalue.ods.api.processors.Specification;
import org.jvalue.ods.processor.specification.SpecificationManager;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.swagger.JsonApiSchema;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.SpecificationWrapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Set;

import static org.jvalue.ods.rest.v2.api.AbstractApi.FILTERTYPES;
import static org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse.JSONAPI_TYPE;
import static org.jvalue.ods.utils.HttpUtils.getDirectoryURI;

@Path(AbstractApi.V2 + "/" + FILTERTYPES)
@Produces(JSONAPI_TYPE)
public final class ProcessorSpecificationApi extends AbstractApi {

	private final SpecificationManager descriptionManager;

	@Context
	private UriInfo uriInfo;

	@Inject
	public ProcessorSpecificationApi(SpecificationManager descriptionManager) {
		this.descriptionManager = descriptionManager;
	}


	@Operation(
		operationId = FILTERTYPES,
		tags = FILTERTYPES,
		summary = "Get all filtertypes",
		description = "Get all types of filterchains registered at the Open-Data-Service" //todo: check
	)
	@ApiResponse(
		responseCode = "200",
		description = "Ok",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.ProcessorSpecificationSchema.class)),
		links = @Link(name = ENTRYPOINT, operationRef = ENTRYPOINT)
	)
	@GET
	public Response getAllSpecifications() {
		Set<Specification> specs = descriptionManager.getAll();

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(SpecificationWrapper.fromCollection(specs))
			.addLink(ENTRYPOINT, getDirectoryURI(uriInfo))
			.build();
	}
}
