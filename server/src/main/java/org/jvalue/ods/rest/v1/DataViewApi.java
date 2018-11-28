package org.jvalue.ods.rest.v1;


import com.google.inject.Inject;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.api.views.couchdb.CouchDbDataViewDescription;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.data.DataViewManager;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(AbstractApi.BASE_URL + "/{sourceId}/views")
@Produces(MediaType.APPLICATION_JSON)
public final class DataViewApi extends AbstractApi {

	private final DataSourceManager sourceManager;
	private final DataViewManager couchDbViewManager;

	@Inject
	public DataViewApi(
			DataSourceManager sourceManager,
			DataViewManager viewManager) {

		this.sourceManager = sourceManager;
		this.couchDbViewManager = viewManager;
	}


	@GET
	public List<CouchDbDataView> getAllViews(@PathParam("sourceId") String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);
		return couchDbViewManager.getAll(source);
	}


	@GET
	@Path("/{viewId}")
	public Object getView(
			@PathParam("sourceId") String sourceId,
			@PathParam("viewId") String viewId,
			@QueryParam("execute") boolean execute,
			@QueryParam("argument") String argument) {
		DataSource source = sourceManager.findBySourceId(sourceId);
		CouchDbDataView view = couchDbViewManager.get(source, viewId);

		if (!execute) return view;
		return couchDbViewManager.executeView(sourceManager.getDataRepository(source), view, argument);
	}


	@PUT
	@Path("/{viewId}")
	public CouchDbDataView addView(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId,
			@PathParam("viewId") String viewId,
			@Valid CouchDbDataViewDescription viewDescription) {
		DataSource source = sourceManager.findBySourceId(sourceId);
		if (couchDbViewManager.contains(source, viewId))
			throw RestUtils.createJsonFormattedException("data view with id " + viewId + " already exists", 409);

		CouchDbDataView view = new CouchDbDataView(viewId, viewDescription.getMapFunction(), viewDescription.getReduceFunction());
		couchDbViewManager.add(source, sourceManager.getDataRepository(source), view);
		return view;
	}


	@DELETE
	@Path("/{viewId}")
	public void deleteView(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId,
			@PathParam("viewId") String viewId) {
		DataSource source = sourceManager.findBySourceId(sourceId);
		CouchDbDataView view = couchDbViewManager.get(source, viewId);
		couchDbViewManager.remove(source, sourceManager.getDataRepository(source), view);
	}
}
