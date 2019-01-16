/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v2.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import org.jvalue.ods.rest.v2.jsonapi.swagger.OpenApiProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.jvalue.ods.rest.v2.api.AbstractApi.V2;

@Path(V2)
@Produces(MediaType.APPLICATION_JSON)
public class SpecificationApi {

	@Context
	private UriInfo uriInfo;

	private final OpenAPI openAPI = OpenApiProvider.getOpenAPI();


	@GET
	@Path("/doc")
	public Response getOpenApi() throws JsonProcessingException {

		return Response
			.ok()
			.entity(Json.mapper().writeValueAsString(openAPI))
			.build();
	}
}
