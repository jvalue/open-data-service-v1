package org.jvalue.ods.rest;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.hubspot.jackson.jaxrs.PropertyFiltering;

import org.jvalue.common.rest.RestUtils;
import org.jvalue.ods.api.data.Data;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.db.DataRepository;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path(AbstractApi.BASE_URL + "/{sourceId}/data")
@Produces(MediaType.APPLICATION_JSON)
public final class DataApi extends AbstractApi {

	private final DataSourceManager sourceManager;

	@Inject
	public DataApi(DataSourceManager sourceManager) {
		this.sourceManager = sourceManager;
	}


	@GET
	@PropertyFiltering(always = {"!result._id", "!result._rev"})
	public Data getObjects(
			@PathParam("sourceId") String sourceId,
			@QueryParam("startId") String startId,
			@QueryParam("count") int count,
			@QueryParam("property") String propertyFilter) {

		if (count < 1 || count > 100) throw RestUtils.createJsonFormattedException("count must be > 0 and < 100", 400);

		DataRepository repository = assertIsValidSource(sourceId);
		return repository.executePaginatedGet(startId, count);
	}


	@DELETE
	public void deleteAllObjects(@PathParam("sourceId") String sourceId) {
		DataRepository repository = assertIsValidSource(sourceId);
		repository.removeAll();
	}


	@GET
	@Path("/{objectId}{pointer:(/.*)?}")
	public JsonNode getObjectAttribute(
			@PathParam("sourceId") String sourceId,
			@PathParam("objectId") String domainId,
			@PathParam("pointer") String pointer) {

		JsonNode node = assertIsValidSource(sourceId).findByDomainId(domainId);
		if (pointer.isEmpty() || pointer.equals("/")) return node;

		try {
			JsonPointer jsonPointer = JsonPointer.compile(pointer);
			JsonNode result = node.at(jsonPointer);
			if (result.isMissingNode()) throw RestUtils.createNotFoundException();
			return result;
		} catch (IllegalArgumentException iae) {
			// thrown by JsonPointer.compile
			throw RestUtils.createNotFoundException();
		}
	}


	private DataRepository assertIsValidSource(String sourceId) {
		return sourceManager.getDataRepository(sourceManager.findBySourceId(sourceId));
	}

}
