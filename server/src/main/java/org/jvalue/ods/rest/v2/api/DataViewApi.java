package org.jvalue.ods.rest.v2.api;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.api.views.DataViewDescription;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.data.DataViewManager;
import org.jvalue.ods.rest.v2.jsonapi.request.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonLinks;
import org.jvalue.ods.rest.v2.jsonapi.swagger.JsonApiSchema;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.DataViewWrapper;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.DataWrapper;
import org.jvalue.ods.utils.JsonMapper;
import org.jvalue.ods.utils.RequestValidator;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static org.jvalue.ods.rest.v2.api.AbstractApi.BASE_URL;
import static org.jvalue.ods.rest.v2.api.AbstractApi.VIEWS;
import static org.jvalue.ods.utils.HttpUtils.getDirectoryURI;
import static org.jvalue.ods.utils.HttpUtils.getSanitizedPath;

@Path(BASE_URL + "/{sourceId}/" + VIEWS)
public final class DataViewApi extends AbstractApi {

	private final DataSourceManager sourceManager;
	private final DataViewManager viewManager;

	@Context
	private UriInfo uriInfo;

	@Inject
	public DataViewApi(
		DataSourceManager sourceManager,
		DataViewManager viewManager) {

		this.sourceManager = sourceManager;
		this.viewManager = viewManager;
	}


	@Operation(
		tags = VIEWS,
		operationId = VIEWS,
		summary = "Get all views",
		description = "Get all data views for a datasource"
	)
	@ApiResponse(
		responseCode = "200", description = "Ok",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.DataViewSchema.class)),
		links = @Link(name = DATASOURCE, operationRef = DATASOURCE, description = "Get corresponding datasource")
	)
	@ApiResponse(
		responseCode = "404", description = "Source not found"
	)
	@GET
	public Response getAllViews(
		@PathParam("sourceId") @Parameter(description = "Id of the corresponding source")
			String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);
		List<DataView> views = viewManager.getAll(source);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataViewWrapper.fromCollection(views))
			.addLink(DATASOURCE, getDirectoryURI(uriInfo))
			.build();
	}


	@Operation(
		tags = VIEWS,
		operationId = VIEW,
		summary = "Get a view",
		description = "Get a view on the data of a source"
	)
	@ApiResponse(
		responseCode = "200", description = "Ok",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.DataViewSchema.class)),
		links = {
			@Link(name = VIEWS, operationRef = VIEWS, description = "Get all views for the source"),
			@Link(name = "execute", operationRef = VIEW, parameters = @LinkParameter(name = "execute", expression = "true"), description = "Execute the view")
		}
	)
	@ApiResponse(
		responseCode = "404", description = "Source or view not found"
	)
	@GET
	@Path("/{viewId}")
	public Response getView(
		@PathParam("sourceId") @Parameter(description = "Id of the corresponding source")
			String sourceId,
		@PathParam("viewId") @Parameter(description = "Id of the view")
			String viewId,
		@QueryParam("execute") @Parameter(description = "If true, get the result of the view's execution on the source data, else get the view itself")
			boolean execute,
		@QueryParam("argument") @Parameter(description = "Optional argument for view execution") String argument) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		DataView view = viewManager.get(source, viewId);

		Response response;

		if (!execute) { //get view
			URI executeURI = uriInfo
				.getAbsolutePathBuilder()
				.queryParam("execute", true)
				.build();

			response = JsonApiResponse
				.createGetResponse(uriInfo)
				.data(DataViewWrapper.from(view))
				.addLink(VIEWS, getDirectoryURI(uriInfo))
				.addLink("execute", executeURI)
				.build();
		} else { //execute view
			List<JsonNode> result = viewManager.executeView(sourceManager.getDataRepository(source), view, argument);

			URI dataURI = getSanitizedPath(uriInfo).resolve("../../data");

			//filter out view itself (is on position 0 of result)
			result = result.subList(1, result.size());

			response = JsonApiResponse
				.createGetResponse(uriInfo)
				.data(DataWrapper.fromCollection(result, source))
				.fromRepositoryURI(dataURI)
				.addLink(VIEW, uriInfo.getAbsolutePath())
				.build();
		}

		return response;
	}


	@Operation(
		tags = VIEWS,
		summary = "Add view",
		description = "Add a data view to a datasource"
	)
	@SecurityRequirement(name = BASICAUTH)
	@ApiResponse(
		responseCode = "201",
		description = "View created",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.DataViewSchema.class)),
		links = @Link(name = VIEWS, operationRef = VIEWS, description = "Get all views for the source"))
	@ApiResponse(responseCode = "401",
		description = "Not authorized")
	@ApiResponse(
		responseCode = "404",
		description = "Source not found"
	)
	@ApiResponse(
		responseCode = "409",
		description = "Data view with viewId already exists"
	)
	@POST
	public Response addView(
		@RestrictedTo(Role.ADMIN) @Parameter(hidden = true)
			User user,
		@PathParam("sourceId")
			String sourceId,
		@RequestBody(
			description = "Description of the DataView to be added.",
			required = true,
			content = @Content(schema = @Schema(implementation = JsonApiSchema.DataViewSchema.class)))
			JsonApiRequest viewDescriptionRequest) {

		DataViewDescription viewDescription = JsonMapper.convertValue(
			viewDescriptionRequest.getAttributes(),
			DataViewDescription.class
		);

		DataSource source = sourceManager.findBySourceId(sourceId);
		assertIsValidViewDescription(viewDescription, viewDescriptionRequest.getId(), source);

		DataView view = new DataView(viewDescriptionRequest.getId(), viewDescription.getMapFunction(), viewDescription.getReduceFunction());
		viewManager.add(source, sourceManager.getDataRepository(source), view);

		URI directoryURI = getSanitizedPath(uriInfo);

		return JsonApiResponse
			.createPostResponse(uriInfo)
			.data(DataViewWrapper.from(view))
			.addLink(JsonLinks.SELF, directoryURI.resolve(viewDescriptionRequest.getId()))
			.addLink(VIEWS, directoryURI)
			.build();
	}


	@Operation(
		tags = VIEWS,
		summary = "Delete a view",
		description = "Delete a view from a datasource"
	)
	@SecurityRequirement(name = BASICAUTH)
	@ApiResponse(
		responseCode = "200",
		description = "View deleted"
	)
	@ApiResponse(responseCode = "401",
		description = "Not authorized")
	@ApiResponse(
		responseCode = "404",
		description = "Source or view not found"
	)
	@DELETE
	@Path("/{viewId}")
	public void deleteView(
		@RestrictedTo(Role.ADMIN) @Parameter(hidden = true)
			User user,
		@PathParam("sourceId") @Parameter(description = "The id of the datasource from which the view should be deleted")
			String sourceId,
		@PathParam("viewId") @Parameter(description = "The id of the view to be deleted")
			String viewId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		DataView view = viewManager.get(source, viewId);
		viewManager.remove(source, sourceManager.getDataRepository(source), view);
	}


	private void assertIsValidViewDescription(DataViewDescription description, String id, DataSource source) {
		RequestValidator.validate(description);

		if (viewManager.contains(source, id)) {
			throw createJsonApiException("data view with id " + id + " already exists", Response.Status.CONFLICT);
		}
	}
}
