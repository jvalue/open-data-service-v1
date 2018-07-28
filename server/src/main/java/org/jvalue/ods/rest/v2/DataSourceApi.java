package org.jvalue.ods.rest.v2;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.inject.Inject;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.DataSourceWrapper;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path(AbstractApi.BASE_URL)
public final class DataSourceApi extends AbstractApi {

	private final DataSourceManager sourceManager;
	@Context
	private UriInfo uriInfo;

	@Inject
	public DataSourceApi(DataSourceManager sourceManager) {
		this.sourceManager = sourceManager;
	}

	@GET
	public Response getAllSources() {
		List<DataSource> sources = sourceManager.getAll();

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataSourceWrapper.fromCollection(sources))
			.build();
	}


	@GET
	@Path("{sourceId}")
	public Response getSource(@PathParam("sourceId") String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataSourceWrapper.from(source))
			.build();
	}


	@GET
	@Path("{sourceId}/schema")
	public Response getSourceSchema(@PathParam("sourceId") String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataSourceWrapper.from(source))
			.restrictTo("schema")
			.build();
	}


	@POST
	@Path("{sourceId}")
	public Response addSource(
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

		return JsonApiResponse
			.createPostResponse(uriInfo)
			.data(DataSourceWrapper.from(source))
			.build();
	}


	@DELETE
	@Path("{sourceId}")
	public Response deleteSource(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId) {

		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}

}
