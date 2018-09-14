package org.jvalue.ods.rest.v2.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jvalue.commons.rest.VersionInfo;
import org.jvalue.ods.GitConstants;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.swagger.JsonApiSchema;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.jvalue.ods.rest.v2.api.AbstractApi.V2;
import static org.jvalue.ods.utils.HttpUtils.getSanitizedPath;

@Path(V2)
public class EntryPoint extends AbstractApi {

	private static final VersionInfo version = new VersionInfo(
		GitConstants.VERSION,
		"https://github.com/jvalue/open-data-service/commit/" + GitConstants.COMMIT_HASH);


	private final EntryPointData data = new EntryPointData();

	@Context private UriInfo uriInfo;

	@Operation(
		tags = ENTRYPOINT,
		operationId = ENTRYPOINT,
		summary = "Entry point",
		description = "Entry point to start navigating through the ODS API"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Ok",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.EntryPointSchema.class)),
		links = {
			@Link(name = DATASOURCES, operationRef = DATASOURCES, description = "Go to datasource api"),
			@Link (name = DOC, operationRef = DOC, description = "Get openapi specification"),
			@Link (name = FILTERTYPES, operationRef = FILTERTYPES, description = "Get types of filters registered at the Open-Data-Service"),
			@Link (name = USERS, operationRef = USERS, description = "Go to user api ")
		}
	)
	@GET
	public Response getApiInfos() {
		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(data)
			.addLink(DATASOURCES, getSanitizedPath(uriInfo).resolve(DATASOURCES))
			.addLink(FILTERTYPES, getSanitizedPath(uriInfo).resolve(FILTERTYPES))
			.addLink(USERS, getSanitizedPath(uriInfo).resolve(USERS))
			.addLink(DOC, getSanitizedPath(uriInfo).resolve(DOC))
			.build();
	}

	public class EntryPointData implements JsonApiIdentifiable{


		@Override
		public String getId() {
			return "jValue_OpenDataService";
		}

		@Override
		public String getType() {
			return "EntryPoint";
		}

		@JsonProperty("API_version")
		public String getApiVersion() {
			return API_VERSION;
		}

		@JsonProperty("ODS_version")
		public VersionInfo getOdsVersion() {
			return version;
		}
	}
}
