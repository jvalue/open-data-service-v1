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

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

@Path(AbstractApi.BASE_URL)
public final class DataSourceApi extends AbstractApi {

	private final DataSourceManager sourceManager;
	@Context
	private UriInfo uriInfo;

	@Inject
	public DataSourceApi(DataSourceManager sourceManager) {
		this.sourceManager = sourceManager;
	}

	@JsonFormat(with = {JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY})
	@GET
	public Response getAllSources() {
		return null;
	}


	@GET
	@Path("/{sourceId}")
	public Response getSource(@PathParam("sourceId") String sourceId) {
		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}


	@GET
	@Path("/{sourceId}/schema")
	public Response getSourceSchema(@PathParam("sourceId") String sourceId) {
		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}


	@POST
	@Path("/{sourceId}")
	public Response addSource(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId,
			@Valid DataSourceDescription sourceDescription) {

		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}


	@DELETE
	@Path("/{sourceId}")
	public Response deleteSource(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId) {

		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}

}
