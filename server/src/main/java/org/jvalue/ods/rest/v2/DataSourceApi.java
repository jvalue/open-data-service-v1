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
		return JsonApiResponse
				.createGetResponse(uriInfo)
				.data(sourceManager.getAll())
				.build();
	}


	@GET
	@Path("/{sourceId}")
	public Response getSource(@PathParam("sourceId") String sourceId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		DataSource other = sourceManager.getAll().get(0);
		URI otherURI = uriInfo.getAbsolutePath().resolve(URI.create(other.getId()));

		return JsonApiResponse
				.createGetResponse(uriInfo)
				.data(source)
				.addRelationship("test", other, otherURI)
				.addIncluded(other, otherURI)
				.build();
	}


	@GET
	@Path("/{sourceId}/schema")
	public Response getSourceSchema(@PathParam("sourceId") String sourceId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		URI sourceURI = uriInfo.getAbsolutePath().resolve(URI.create("./"));
		return JsonApiResponse
				.createGetResponse(uriInfo)
				.data(source)
				.restrictTo("schema")
				.addLink("source", sourceURI)
				.build();
	}


	@POST
	@Path("/{sourceId}")
	public Response addSource(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId,
			@Valid DataSourceDescription sourceDescription) {

		assertDataSourceNotExist(sourceId);

		DataSource source = new DataSource(
				sourceId,
				sourceDescription.getDomainIdKey(),
				sourceDescription.getSchema(),
				sourceDescription.getMetaData());

		sourceManager.add(source);

		return JsonApiResponse.createPostResponse(uriInfo)
				.data(source)
				.build();
	}


	@DELETE
	@Path("/{sourceId}")
	public void deleteSource(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId) {

		sourceManager.remove(sourceManager.findBySourceId(sourceId));
	}


	private void assertDataSourceNotExist(String sourceId) {
		if (sourceManager.isValidSourceId(sourceId)) {
			int statusCode = Response.Status.CONFLICT.getStatusCode();
			throw RestUtils.createJsonFormattedException("source with id " + sourceId + " already exists", statusCode);
		}
	}

}
