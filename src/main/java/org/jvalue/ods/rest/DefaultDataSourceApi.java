package org.jvalue.ods.rest;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.primitives.Ints;
import com.google.inject.Inject;

import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.db.SourceDataRepository;
import org.jvalue.ods.utils.JsonPropertyKey;

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


	@GET
	@Path("/{objectId}{attribute:(/.*)?}")
	public JsonNode getObjectAttribute(
			@PathParam("sourceId") String sourceId,
			@PathParam("objectId") String objectId,
			@PathParam("attribute") String attribute) {

		JsonNode node = getSingleObject(sourceId, objectId);
		if (attribute.isEmpty() || attribute.equals("/")) return node;

		// remove the first dash
		String[] pathElements = attribute.substring(1).split("/");
		JsonPropertyKey.Builder keyBuilder = new JsonPropertyKey.Builder();
		for (String pathElement : pathElements) {
			// check for array indices
			Integer idx = Ints.tryParse(pathElement);
			if (idx != null) keyBuilder.intPath(idx.intValue());
			else keyBuilder.stringPath(pathElement);
		}

		JsonNode result = keyBuilder.build().getProperty(node);
		if (result == null) throw RestUtils.createNotFoundException();
		return result;
	}


	private JsonNode getSingleObject(String sourceId, String objectId) {
		assertIsValidSource(sourceId);
		SourceDataRepository repo = sourceManager.getDataRepositoryForSourceId(sourceId);
		if (!repo.contains(objectId)) throw RestUtils.createNotFoundException();
		return sourceManager.getDataRepositoryForSourceId(sourceId).get(objectId);
	}


	private void assertIsValidSource(String sourceId) {
		if (sourceManager.getDataRepositoryForSourceId(sourceId) == null) {
			throw RestUtils.createNotFoundException();
		}
	}

}
