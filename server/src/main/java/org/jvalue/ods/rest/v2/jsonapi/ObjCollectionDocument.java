package org.jvalue.ods.rest.v2.jsonapi;

import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObjCollectionDocument extends JsonApiDocument {

	private List<JsonApiData> data;

	public ObjCollectionDocument(Collection<? extends JsonApiIdentifiable> entityCollection, UriInfo uriInfo) {
		super(uriInfo);
		this.data = createJsonApiCollection(entityCollection);
	}

	public ObjCollectionDocument(List<JsonApiData> data, UriInfo uriInfo) {
		super(uriInfo);
		this.data = data;
	}

	private List<JsonApiData> createJsonApiCollection(Collection<? extends JsonApiIdentifiable> entityCollection) {

		List<JsonApiData> jResourceList = new ArrayList<>(entityCollection.size());
		entityCollection
				.forEach(e ->
						jResourceList
								.add(new JsonApiResource(e, createSelfLink(e))
										.setSelfLink())
				);
		return jResourceList;

	}

	private URI createSelfLink(JsonApiIdentifiable obj) {
		return uriInfo.getAbsolutePath().resolve(obj.getId());
	}


	@Override
	public ObjCollectionDocument toIdentifier() {

		List<JsonApiData> identifierData = new ArrayList<>();

		data.forEach(e -> identifierData.add(e.toIdentifier()));

		return new ObjCollectionDocument(identifierData, uriInfo);
	}


	@Override
	public Object getData() {
		return data;
	}
}
