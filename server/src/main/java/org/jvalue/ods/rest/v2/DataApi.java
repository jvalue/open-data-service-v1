package org.jvalue.ods.rest.v2;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.hubspot.jackson.jaxrs.PropertyFiltering;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.ods.api.data.Data;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.DataNode;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.DataWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.jvalue.ods.rest.v2.AbstractApi.BASE_URL;

@Path(BASE_URL + "/{sourceId}/data")
public final class DataApi extends AbstractApi {

	private final DataSourceManager sourceManager;

	@Inject
	public DataApi(DataSourceManager sourceManager) {
		this.sourceManager = sourceManager;
	}

	@Context
	private UriInfo uriInfo;


	@GET
	@PropertyFiltering(always = {"!data.attributes.result._id", "!data.attributes.result._rev"})
	public Response getObjects(
			@PathParam("sourceId") String sourceId,
			@QueryParam("startId") String startId,
			@QueryParam("count") int count,
			@QueryParam("property") String propertyFilter) {

		//statt data objects einfach eigene verwenden?
		if (count < 1 || count > 100) throw RestUtils.createJsonFormattedException("count must be > 0 and <= 100", 400);

		DataRepository repository = getDataRepository(sourceId);
		Data data = repository.executePaginatedGet(startId, count);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataWrapper.from(data))
			.build();
	}


	@DELETE
	public void deleteAllObjects(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId) {

		DataRepository repository = getDataRepository(sourceId);
		repository.removeAll();
	}


	@GET
	@Path("/{objectId}")
	public Response getSingleDataObject(
			@PathParam("sourceId") String sourceId,
			@PathParam("objectId") String domainId) {

		JsonNode resultNode = getDataRepository(sourceId).findByDomainId(domainId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataNode.from(resultNode))
			.build();
	}

	@GET
	@Path("/{objectId}/{attribute}")
	public Response getObjectAttribute(
		@PathParam("sourceId") String sourceId,
		@PathParam("objectId") String domainId,
		@PathParam("attribute") String attribute) {

		JsonNode resultNode = getDataRepository(sourceId).findByDomainId(domainId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(DataNode.from(resultNode))
			.restrictTo(attribute)
			.build();
	}

	private DataRepository getDataRepository(String sourceId) {
		return sourceManager.getDataRepository(sourceManager.findBySourceId(sourceId));
	}

}
