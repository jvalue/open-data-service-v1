package org.jvalue.ods.rest.v2.jsonapi.response;

import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import java.net.URI;
import java.util.Collection;

/**
 * Interface for objects that match the "data" field of the JSONAPI specification (top level element)
 * May be realized by either a single resource object (JsonApiResource) or an array of resource objects (JsonApiResourceCollection).
 */
interface JsonApiData {

	JsonApiData restrictTo(String attribute);

	JsonApiData addRelationship(String name, Collection<? extends JsonApiIdentifiable> relatedCollection, URI location);

	JsonApiData addRelationship(String name, JsonApiIdentifiable related, URI location);

	URI getRelationshipUri(JsonApiIdentifiable related);

	boolean hasRelationshipTo(JsonApiIdentifiable related);

	void setResourceCollectionURI(URI collectionURI);
}
