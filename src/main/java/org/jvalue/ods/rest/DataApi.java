package org.jvalue.ods.rest;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.db.DataRepository;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AbstractApi.BASE_URL + "/{sourceId}/data")
@Produces(MediaType.APPLICATION_JSON)
public final class DataApi extends AbstractApi {

	private final DataSourceManager sourceManager;
	private final JsonDbPropertyFilter jsonFilter = new JsonDbPropertyFilter();

	@Inject
	public DataApi(DataSourceManager sourceManager) {
		this.sourceManager = sourceManager;
	}


	@GET
	public List<Object> getAll(@PathParam("sourceId") String sourceId) throws Exception {
		DataRepository repository = assertIsValidSource(sourceId);
		return new LinkedList<Object>(jsonFilter.filter(repository.getAll()));
	}


	@GET
	@Path("/{objectId}{pointer:(/.*)?}")
	public JsonNode getObjectAttribute(
			@PathParam("sourceId") String sourceId,
			@PathParam("objectId") String domainId,
			@PathParam("pointer") String pointer) {

		JsonNode node = assertIsValidSource(sourceId).findByDomainId(domainId);
		if (pointer.isEmpty() || pointer.equals("/")) return jsonFilter.filter(node);

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
