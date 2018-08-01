package org.jvalue.ods.rest.v2;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.api.views.DataViewDescription;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.data.DataViewManager;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.DataViewWrapper;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.DataWrapper;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static org.jvalue.ods.rest.v2.AbstractApi.BASE_URL;
import static org.jvalue.ods.utils.HttpUtils.getDirectoryURI;
import static org.jvalue.ods.utils.HttpUtils.getSanitizedPath;

@Path(BASE_URL + "/{sourceId}/views")
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


	@GET
	public Response getAllViews(@PathParam("sourceId") String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);
		List<DataView> views = viewManager.getAll(source);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataViewWrapper.fromCollection(views))
			.addLink("source", getDirectoryURI(uriInfo))
			.build();
	}


	@GET
	@Path("/{viewId}")
	public Response getView(
		@PathParam("sourceId") String sourceId,
		@PathParam("viewId") String viewId,
		@QueryParam("execute") boolean execute,
		@QueryParam("argument") String argument) {

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
				.addLink("views", getDirectoryURI(uriInfo))
				.addLink("execute", executeURI)
				.build();
		}
		else { //execute view
			List<JsonNode> result = viewManager.executeView(sourceManager.getDataRepository(source), view, argument);

			URI dataURI = getSanitizedPath(uriInfo).resolve("../../data");

			//filter out view itself (is on position 0 of result)
			result = result.subList(1, result.size());

			response = JsonApiResponse
				.createGetResponse(uriInfo)
				.data(DataWrapper.fromCollection(result, source))
				.fromRepositoryURI(dataURI)
				.addLink("view", uriInfo.getAbsolutePath())
				.build();
		}

		return response;
	}


	@POST
	@Path("/{viewId}")
	public Response addView(
		@RestrictedTo(Role.ADMIN) User user,
		@PathParam("sourceId") String sourceId,
		@PathParam("viewId") String viewId,
		@Valid DataViewDescription viewDescription) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		if (viewManager.contains(source, viewId))
			throw RestUtils.createJsonFormattedException("data view with id " + viewId + " already exists", 409);

		DataView view = new DataView(viewId, viewDescription.getMapFunction(), viewDescription.getReduceFunction());
		viewManager.add(source, sourceManager.getDataRepository(source), view);
		return JsonApiResponse
			.createPostResponse(uriInfo)
			.data(DataViewWrapper.from(view))
			.addLink("views", getDirectoryURI(uriInfo))
			.build();
	}


	@DELETE
	@Path("/{viewId}")
	public void deleteView(
		@RestrictedTo(Role.ADMIN) User user,
		@PathParam("sourceId") String sourceId,
		@PathParam("viewId") String viewId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		DataView view = viewManager.get(source, viewId);
		viewManager.remove(source, sourceManager.getDataRepository(source), view);
	}

}
