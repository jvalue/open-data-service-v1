package org.jvalue.ods.rest;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.DataRepositoryCache;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/datasources/{sourceId}")
@Produces(MediaType.APPLICATION_JSON)
public class DefaultDataSourceApi {

	private final DataRepositoryCache dataRepositoryCache;

	@Inject
	public DefaultDataSourceApi(DataRepositoryCache dataRepositoryCache) {
		this.dataRepositoryCache = dataRepositoryCache;
	}


	@GET
	public List<Object> getAll(@PathParam("sourceId") String sourceId) throws Exception {
		DataRepository repository = assertIsValidSource(sourceId);
		return new LinkedList<Object>(repository.getAll());
	}


	@GET
	@Path("/{objectId}{pointer:(/.*)?}")
	public JsonNode getObjectAttribute(
			@PathParam("sourceId") String sourceId,
			@PathParam("objectId") String objectId,
			@PathParam("pointer") String pointer) {

		JsonNode node = getSingleObject(sourceId, objectId);
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


	private JsonNode getSingleObject(String sourceId, String objectId) {
		DataRepository repository = assertIsValidSource(sourceId);
		if (!repository.contains(objectId)) throw RestUtils.createNotFoundException();
		return repository.get(objectId);
	}


	private DataRepository assertIsValidSource(String sourceId) {
		DataRepository repository = dataRepositoryCache.getRepositoryForSourceId(sourceId);
		if (repository == null)  throw RestUtils.createNotFoundException();
		return repository;
	}

}
