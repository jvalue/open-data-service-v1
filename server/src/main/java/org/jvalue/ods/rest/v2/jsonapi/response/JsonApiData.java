/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
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

	/**
	 * Get the resourceObject representation of this data object.
	 * @return this, if the object is an instance of JsonApiResource, the first (and only) element of the collection if it is a one element resource collection.
	 * @throws IllegalStateException if the data object is a resource collection containing zero or more than one elements.
	 */
	JsonApiResource asSingleResource() throws IllegalStateException;

	/**
	 * Get the resourceCollection representation of this data object.
	 * @return this, if the object is an instance of JsonApiResourceCollection, a one element collection containing the resource object otherwise.
	 */
	JsonApiResourceCollection asResourceCollection();
}
