package org.jvalue.ods.rest;


import com.google.inject.Inject;

import org.jvalue.ods.data.DataSourceManager;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/datasource/{sourceId}")
@Produces(MediaType.APPLICATION_JSON)
public class DefaultDataSourceApi {

	private final DataSourceManager sourceManager;

	@Inject
	public DefaultDataSourceApi(DataSourceManager sourceManager) {
		this.sourceManager = sourceManager;
	}


	@GET
	public List<Object> getAll(@PathParam("sourceId") String sourceId) throws Exception {
		assertIsValidSource(sourceId);
		return new LinkedList<Object>(sourceManager.getDataRepositoryForSourceId(sourceId).getAll());
	}


	private void assertIsValidSource(String sourceId) {
		if (sourceManager.getDataRepositoryForSourceId(sourceId) == null) {
			throw RestUtils.createJsonFormattedException("invalid source id", 404);
		}
	}

}
