package org.jvalue.ods.rest.v2.jsonapi.response;

import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;
import org.jvalue.ods.rest.v2.jsonapi.document.JsonApiDocument;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

public class JsonApiResponse {

	private UriInfo uriInfo;
	private JsonApiDocument jsonApiEntity;
	private Response.StatusType statusCode;

	private JsonApiResponse(UriInfo uriInfo, Response.StatusType statusCode) {
		this.uriInfo = uriInfo;
		this.statusCode = statusCode;
	}


	public static RequiredEntity createGetResponse(UriInfo uriInfo) {
		return new Builder(new JsonApiResponse(uriInfo, Response.Status.OK));
	}


	public static RequiredEntity createPostResponse(UriInfo uriInfo) {
		return new Builder(new JsonApiResponse(uriInfo, Response.Status.CREATED));
	}


	public static RequiredEntity createPutResponse(UriInfo uriInfo) {
		return new Builder(new JsonApiResponse(uriInfo, Response.Status.OK));
	}


	public static class Builder implements RequiredEntity, WithRelationship, Buildable {

		private final JsonApiResponse instance;

		public Builder(JsonApiResponse instance) {
			this.instance = instance;
		}


		@Override
		public Buildable data(JsonApiIdentifiable entity) {
			instance.jsonApiEntity = new JsonApiDocument(entity, instance.uriInfo);
			instance.jsonApiEntity.addSelfLink(instance.uriInfo);
			return this;
		}


		@Override
		public Buildable data(Collection<? extends JsonApiIdentifiable> entityCollection) {
			instance.jsonApiEntity = new JsonApiDocument(entityCollection, instance.uriInfo);
			instance.jsonApiEntity.addSelfLink(instance.uriInfo);
			return this;
		}


		public Buildable addLink(String name, URI ref) {
			instance.jsonApiEntity.addLink(name, ref);
			return this;
		}

		@Override
		public WithRelationship addRelationship(String name, JsonApiIdentifiable entity, URI location) {
			instance.jsonApiEntity.addRelationship(name, entity, location);
			return this;
		}


		@Override
		public Buildable restrictTo(String attribute) {
			instance.jsonApiEntity.restrictTo(attribute);
			return this;
		}


		@Override
		public WithRelationship addIncluded(JsonApiIdentifiable included, URI location) {
			assertHasRelationship(included);

			instance.jsonApiEntity.addIncluded(included, location);
			return this;
		}


		@Override
		public Response build() {
			Response.ResponseBuilder responseBuilder = Response.status(instance.statusCode);
			if (instance.jsonApiEntity != null) {
				responseBuilder
						.type(JsonApiMediaType.JSONAPI)
						.entity(instance.jsonApiEntity);
			}
			return responseBuilder.build();
		}

		private void assertHasRelationship(JsonApiIdentifiable relationship) {
			if(!instance.jsonApiEntity.hasRelationshipTo(relationship)) {
				throw new IllegalArgumentException(
					"It is not supported to add includes to resources with which there is no prior relationship. " +
						"Try adding it as relationship first.");
			}
		}

	}

	/**
	 * Interface for a Responsebuilder that needs an entity for further processing
	 */
	public interface RequiredEntity {
		/**
		 * Add a single entity to a response.
		 * @param entity the entity to add.
		 * @return A buildable Responsebuilder.
		 */
		Buildable data(JsonApiIdentifiable entity);

		Buildable data(Collection<? extends JsonApiIdentifiable> entityCollection);
	}

	/**
	 * Interface for a Responsebuilder that meets all requirements to build the response.
	 */
	public interface Buildable {
		Response build();

		/**
		 * Restricts the serialization of the generated response body to a certain attribute,
		 * i.e. creates a view which removes every attribute except type, id, selflink and attribute
		 * @param attribute the name of the attribute which should appear
		 * @return a buildable Responsebuilder.
		 */
		Buildable restrictTo(String attribute);

		/**
		 * Adds a link on document level to the generated response body.
		 * @param name the name of the link.
		 * @param ref the reference of the link.
		 * @return a buildable Responsebuilder.
		 */
		Buildable addLink(String name, URI ref);

		/**
		 * Adds a relationship to the generated response body. The corresponding entity is represented as
		 * a ResourceIdentifier with a selflink on resource level.
		 * @param name the name of the relationship in the generated response body.
		 * @param entity the corresponding entity to which the relationship should be added.
		 * @param location the endpoint where the entity is located.
		 * @return a buildable Responsebuilder on which addIncluded() can be called.
		 */
		WithRelationship addRelationship(String name, JsonApiIdentifiable entity, URI location);
	}


	/**
	 * Interface for a Responsebuilder which has at least one relationship added.
	 * It is needed to ensure that included entities can only be added to responses that contain relationships.
	 * Extends Buildable since it can only be returned if the Responsebuilder already contains an entity
	 */
	public interface WithRelationship extends Buildable{
		/**
		 * Adds a included resource to the generated response body.
		 * @param included the entity to be included. Entity has to be added as a relationship before.
		 * @param location the location of the entity
		 * @return a buildable ResponseBuilder on which addIncluded() can be called.
		 */
		WithRelationship addIncluded(JsonApiIdentifiable included, URI location);
	}

}

