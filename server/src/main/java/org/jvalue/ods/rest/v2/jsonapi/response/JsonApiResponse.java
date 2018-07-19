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


	public static class Builder implements RequiredEntity, Buildable {

		private final JsonApiResponse instance;

		public Builder(JsonApiResponse instance) {
			this.instance = instance;
		}


		@Override
		public Buildable data(JsonApiIdentifiable entity) {
			instance.jsonApiEntity = new JsonApiDocument(entity, instance.uriInfo);
			return this;
		}


		@Override
		public Buildable data(Collection<? extends JsonApiIdentifiable> entityCollection) {
			instance.jsonApiEntity = new JsonApiDocument(entityCollection, instance.uriInfo);
			return this;
		}


		public Buildable addLink(String name, URI ref) {
			instance.jsonApiEntity.addLink(name, ref);
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
	}


	public interface RequiredEntity {
		Buildable data(JsonApiIdentifiable entity);

		Buildable data(Collection<? extends JsonApiIdentifiable> entityCollection);
	}


	public interface Buildable {
		Response build();

		Buildable addLink(String name, URI ref);
	}

}

