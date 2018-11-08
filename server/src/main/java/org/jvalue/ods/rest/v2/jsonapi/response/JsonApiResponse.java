package org.jvalue.ods.rest.v2.jsonapi.response;

import org.jvalue.commons.utils.Assert;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

public class JsonApiResponse {

	public static final String JSONAPI_TYPE = "application/vnd.api+json";

	private UriInfo uriInfo;
	private JsonApiDocument jsonApiEntity;
	private Response.StatusType statusCode;

	private JsonApiResponse(UriInfo uriInfo, Response.StatusType statusCode) {
		Assert.assertNotNull(uriInfo, statusCode);

		this.uriInfo = uriInfo;
		this.statusCode = statusCode;
	}

	/**
	 * Constructor for JsonApiResponses without UriInfo (i.e. exceptions)
	 * @param statusCode the http status code of the response
	 */
	private JsonApiResponse(Response.StatusType statusCode) {
		Assert.assertNotNull(statusCode);

		this.statusCode = statusCode;
		JsonApiError error = new JsonApiError(
			statusCode.getReasonPhrase(), statusCode.getStatusCode());
		this.jsonApiEntity = new JsonApiDocument(error);
	}


	public static RequiredEntity createGetResponse(UriInfo uriInfo) {
		Assert.assertNotNull(uriInfo);

		return new Builder(new JsonApiResponse(uriInfo, Response.Status.OK));
	}


	public static RequiredEntity createPostResponse(UriInfo uriInfo) {
		Assert.assertNotNull(uriInfo);

		return new Builder(new JsonApiResponse(uriInfo, Response.Status.CREATED));
	}


	public static RequiredEntity createPutResponse(UriInfo uriInfo) {
		Assert.assertNotNull(uriInfo);

		return new Builder(new JsonApiResponse(uriInfo, Response.Status.OK));
	}


	public static BuildableException createExceptionResponse(int code) {
		assertIsValidErrCode(code);

		return new Builder(new JsonApiResponse(Response.Status.fromStatusCode(code)));
	}

	private static void assertIsValidErrCode(int code) {
		if(code > 500 || code < 400) {
			throw new IllegalArgumentException(code + " is not a valid HTTP Error Code");
		}
	}


	public static class Builder implements RequiredEntity, WithRelationship, Buildable, BuildableException {

		private final JsonApiResponse instance;

		public Builder(JsonApiResponse instance) {
			this.instance = instance;
		}


		@Override
		public Buildable data(JsonApiIdentifiable entity) {
			Assert.assertNotNull(entity);

			instance.jsonApiEntity = new JsonApiDocument(entity, instance.uriInfo);
			instance.jsonApiEntity.addSelfLink();
			return this;
		}


		@Override
		public Buildable data(Collection<? extends JsonApiIdentifiable> entityCollection) {
			Assert.assertNotNull(entityCollection);

			instance.jsonApiEntity = new JsonApiDocument(entityCollection, instance.uriInfo);
			instance.jsonApiEntity.addSelfLink();
			return this;
		}


		@Override
		public Buildable fromRepositoryURI(URI uri) {
			Assert.assertNotNull(uri);

			instance.jsonApiEntity.setResourceCollectionURI(uri);

			return this;
		}


		public Buildable addLink(String name, URI ref) {
			Assert.assertNotNull(name, ref);

			instance.jsonApiEntity.addLink(name, ref);
			return this;
		}


		@Override
		public WithRelationship addRelationship(String name, JsonApiIdentifiable entity, URI location) {
			Assert.assertNotNull(name, entity, location);

			instance.jsonApiEntity.addRelationship(name, entity, location);
			return this;
		}

		@Override
		public WithRelationship addRelationship(String name, Collection<? extends JsonApiIdentifiable> entityCollection, URI location) {
			Assert.assertNotNull(entityCollection);
			entityCollection.forEach(Assert::assertNotNull);

			instance.jsonApiEntity.addRelationship(name, entityCollection, location);
			return this;
		}


		@Override
		public Buildable restrictTo(String attribute) {
			Assert.assertNotNull(attribute);

			instance.jsonApiEntity.restrictTo(attribute);
			return this;
		}


		@Override
		public WithRelationship addIncluded(JsonApiIdentifiable included) {
			assertHasRelationship(included);

			instance.jsonApiEntity.addIncluded(included);
			return this;
		}


		@Override
		public Response build() {
			Response.ResponseBuilder responseBuilder = Response.status(instance.statusCode);
			if (instance.jsonApiEntity != null) {
				responseBuilder
					.type(JSONAPI_TYPE)
					.entity(instance.jsonApiEntity);
			}
			return responseBuilder.build();
		}

		@Override
		public BuildableException message(String message) {
			JsonApiError error = new JsonApiError(
				instance.statusCode.getReasonPhrase() + ": " + message,
				instance.statusCode.getStatusCode());
			instance.jsonApiEntity = new JsonApiDocument(error);

			return this;
		}


		private void assertHasRelationship(JsonApiIdentifiable relationship) {
			if (!instance.jsonApiEntity.hasRelationshipTo(relationship)) {
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
		 *
		 * @param entity the entity to add.
		 * @return A buildable Responsebuilder.
		 */
		Buildable data(JsonApiIdentifiable entity);

		Buildable data(Collection<? extends JsonApiIdentifiable> entityCollection);
	}

	/**
	 * Interface for a Responsebuilder that meets all requirements to build a exception response.
	 */
	public interface BuildableException {
		Response build();

		/**
		 * Add a custom defined message to the exception response
		 * @param message the message to be included. It will be added after the default reason phrase for the provided error code.
		 * @return A buildable Responsebuilder for exception responses.
		 */
		BuildableException message(String message);
	}

	/**
	 * Interface for a Responsebuilder that meets all requirements to build the response.
	 */
	public interface Buildable {
		Response build();

		/**
		 * Restricts the serialization of the generated response body to a certain attribute,
		 * i.e. creates a view which removes every attribute except type, id, selflink and attribute
		 *
		 * @param attribute the name of the attribute which should appear
		 * @return a buildable Responsebuilder.
		 */
		Buildable restrictTo(String attribute);


		/**
		 * Sets the location for the responses entity.
		 *
		 * @param uri the location of the entity
		 * @return a buildable ResponseBuilder
		 */
		Buildable fromRepositoryURI(URI uri);


		/**
		 * Adds a link on document level to the generated response body.
		 *
		 * @param name the name of the link.
		 * @param ref  the reference of the link.
		 * @return a buildable Responsebuilder.
		 */
		Buildable addLink(String name, URI ref);

		/**
		 * Adds a relationship to the generated response body. The corresponding entity is represented as
		 * a ResourceIdentifier with a selflink on resource level.
		 *
		 * @param name     the name of the relationship in the generated response body.
		 * @param entity   the corresponding entity to which the relationship should be added.
		 * @param location the endpoint where the entity is located.
		 * @return a buildable Responsebuilder on which addIncluded() can be called.
		 */
		WithRelationship addRelationship(String name, JsonApiIdentifiable entity, URI location);

		/**
		 * Adds a relationship to the generated response body. The corresponding collection of entities is represented as
		 * a collection of ResourceIdentifiers with selflinks on resource level.
		 *
		 * @param name             the name of the relationship in the generated response body.
		 * @param entityCollection the collection of entities to which the relationship should be added
		 * @param location         the endpoint where the entity is located.
		 * @return a buildable Responsebuilder on which addIncluded() can be called.
		 */
		WithRelationship addRelationship(String name, Collection<? extends JsonApiIdentifiable> entityCollection, URI location);
	}


	/**
	 * Interface for a Responsebuilder which has at least one relationship added.
	 * It is needed to ensure that included entities can only be added to responses that contain relationships.
	 * Extends Buildable since it can only be returned if the Responsebuilder already contains an entity
	 */
	public interface WithRelationship extends Buildable {
		/**
		 * Adds a included resource to the generated response body.
		 *
		 * @param included the entity to be included. Entity has to be added as a relationship before.
		 * @return a buildable ResponseBuilder on which addIncluded() can be called.
		 */
		WithRelationship addIncluded(JsonApiIdentifiable included);
	}

}

