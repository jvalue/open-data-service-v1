package org.jvalue.ods.rest.v1;


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

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path(AbstractApi.BASE_URL + "/{sourceId}/views")
@Produces(MediaType.APPLICATION_JSON)
public final class DataViewApi extends AbstractApi {

	private final DataSourceManager sourceManager;
	private final DataViewManager viewManager;

	@Inject
	public DataViewApi(
			DataSourceManager sourceManager,
			DataViewManager viewManager) {

		this.sourceManager = sourceManager;
		this.viewManager = viewManager;
	}


	@GET
	public List<DataView> getAllViews(@PathParam("sourceId") String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);
		return viewManager.getAll(source);
	}


	@GET
	@Path("/{viewId}")
	public Object getView(
			@PathParam("sourceId") String sourceId,
			@PathParam("viewId") String viewId,
			@QueryParam("execute") boolean execute,
			@QueryParam("argument") String argument) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		DataView view = viewManager.get(source, viewId);

		if (!execute) return view;
		return viewManager.executeView(sourceManager.getDataRepository(source), view, argument);
	}


	@PUT
	@Path("/{viewId}")
	public DataView addView(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId,
			@PathParam("viewId") String viewId,
			@Valid DataViewDescription viewDescription) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		if (viewManager.contains(source, viewId))
			throw RestUtils.createJsonFormattedException("data view with id " + viewId + " already exists", 409);

		DataView view = new DataView(viewId, viewDescription.getMapFunction(), viewDescription.getReduceFunction());
		viewManager.add(source, sourceManager.getDataRepository(source), view);
		return view;
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
