package org.jvalue.ods.rest.v1;


import com.google.inject.Inject;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.commons.utils.HttpServiceCheck;
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

	private static final String COUCH_DB_NOT_AVAILABLE = "CouchDbView Api is not available. No CouchDB found. Use Transformation Api instead.";

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
	public List<CouchDbDataView> getAllViews(@PathParam("sourceId") String sourceId) {
		assertCouchDbIsAvailable();
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
		assertCouchDbIsAvailable();
		DataSource source = sourceManager.findBySourceId(sourceId);
		CouchDbDataView view = viewManager.get(source, viewId);

		if (!execute) return view;
		return viewManager.executeView(sourceManager.getDataRepository(source), view, argument);
	}


	@PUT
	@Path("/{viewId}")
	public CouchDbDataView addView(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId,
			@PathParam("viewId") String viewId,
			@Valid CouchDbDataViewDescription viewDescription) {
		assertCouchDbIsAvailable();
		DataSource source = sourceManager.findBySourceId(sourceId);
		if (viewManager.contains(source, viewId))
			throw RestUtils.createJsonFormattedException("data view with id " + viewId + " already exists", 409);

		CouchDbDataView view = new CouchDbDataView(viewId, viewDescription.getMapFunction(), viewDescription.getReduceFunction());
		viewManager.add(source, sourceManager.getDataRepository(source), view);
		return view;
	}


	@DELETE
	@Path("/{viewId}")
	public void deleteView(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId,
			@PathParam("viewId") String viewId) {
		assertCouchDbIsAvailable();
		DataSource source = sourceManager.findBySourceId(sourceId);
		CouchDbDataView view = viewManager.get(source, viewId);
		viewManager.remove(source, sourceManager.getDataRepository(source), view);
	}


	private void assertCouchDbIsAvailable() {
		if(!HttpServiceCheck.check(HttpServiceCheck.COUCHDB_URL, 1, 0)){
			throw RestUtils.createJsonFormattedException(COUCH_DB_NOT_AVAILABLE, 503);
		}
	}

}
