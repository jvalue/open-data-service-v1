/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v1;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.data.DataSourceManager;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(AbstractApi.BASE_URL)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class DataSourceApi extends AbstractApi {

	private final DataSourceManager sourceManager;

	@Inject
	public DataSourceApi(DataSourceManager sourceManager) {
		this.sourceManager = sourceManager;
	}


	@GET
	public List<DataSource> getAllSources() {
		return sourceManager.getAll();
	}


	@GET
	@Path("/{sourceId}")
	public DataSource getSource(@PathParam("sourceId") String sourceId) {
		return sourceManager.findBySourceId(sourceId);
	}


	@GET
	@Path("/{sourceId}/schema")
	public JsonNode getSourceSchema(@PathParam("sourceId") String sourceId) {
		return sourceManager.findBySourceId(sourceId).getSchema();
	}


	@PUT
	@Path("/{sourceId}")
	public DataSource addSource(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId,
			@Valid DataSourceDescription sourceDescription) {

		if (sourceManager.isValidSourceId(sourceId))
			throw RestUtils.createJsonFormattedException("source with id " + sourceId + " already exists", 409);

		DataSource source = new DataSource(
				sourceId,
				sourceDescription.getDomainIdKey(),
				sourceDescription.getSchema(),
				sourceDescription.getMetaData());
		sourceManager.add(source);
		return source;
	}


	@DELETE
	@Path("/{sourceId}")
	public void deleteSource(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId) {

		sourceManager.remove(sourceManager.findBySourceId(sourceId));
	}

}
