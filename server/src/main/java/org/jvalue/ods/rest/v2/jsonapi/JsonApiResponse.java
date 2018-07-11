package org.jvalue.ods.rest.v2.jsonapi;

import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
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


	public static class Builder implements RequiredEntity, Buildable {

		private final JsonApiResponse instance;

		public Builder(JsonApiResponse instance) {
			this.instance = instance;
		}


		@Override
		public Buildable data(JsonApiIdentifiable entity) {
			instance.jsonApiEntity = new SingleObjectDocument(entity, instance.uriInfo);
			return this;
		}


		@Override
		public Buildable data(Collection<? extends JsonApiIdentifiable> entityCollection) {
			instance.jsonApiEntity = new ObjCollectionDocument(entityCollection, instance.uriInfo);
			return this;
		}


        public Buildable addLink(String name, String url) {
			//TODO: impl.
			return this;
		}


		public Buildable addRelationship(JsonApiIdentifiable entity) {
			//TODO: impl.
			return this;
		}


		@Override
		public Response build() {
			Response.ResponseBuilder responseBuilder = Response.status(instance.statusCode);
			if(instance.jsonApiEntity != null) {
				responseBuilder
						.type(JsonApiMediaType.JSONAPI)
						.entity(instance.jsonApiEntity);
			}
			return responseBuilder.build();
		}

        @Override
        public Buildable toIdentifier() {
            instance.jsonApiEntity.toIdentifier();
            return this;
        }

    }


	public interface RequiredEntity {
		Buildable data(JsonApiIdentifiable entity);
        Buildable data(Collection<? extends JsonApiIdentifiable> entityCollection);
	}


	public interface Buildable {
		Response build();
		Buildable toIdentifier();
		Buildable addLink(String name, String url);
		Buildable addRelationship(JsonApiIdentifiable entity);
	}

}

