package org.jvalue.ods.rest.v1;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.hubspot.jackson.jaxrs.PropertyFiltering;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.db.GenericDataRepository;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.commons.db.data.Data;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.data.DataSourceManager;

import javax.ws.rs.*;
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

		GenericDataRepository<CouchDbDataView, JsonNode> repository = assertIsValidSource(sourceId);
		return repository.getPaginatedData(startId, count);
	}


	@DELETE
	public void deleteAllObjects(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId) {

		GenericDataRepository<CouchDbDataView, JsonNode> repository = assertIsValidSource(sourceId);
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


	private GenericDataRepository<CouchDbDataView, JsonNode> assertIsValidSource(String sourceId) {
		return sourceManager.getDataRepository(sourceManager.findBySourceId(sourceId));
	}

}
