package org.jvalue.ods.rest.v2;


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

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static org.jvalue.ods.rest.v2.AbstractApi.BASE_URL;

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

		if (!execute) {
			return JsonApiResponse
				.createGetResponse(uriInfo)
				.data(DataViewWrapper.from(view))
				.build();
		}
		else {
			return Response.status(Response.Status.NOT_IMPLEMENTED).build();
		}
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
