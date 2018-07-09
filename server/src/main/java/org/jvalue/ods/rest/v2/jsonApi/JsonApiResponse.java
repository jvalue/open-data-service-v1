package org.jvalue.ods.rest.v2.jsonApi;

import org.jvalue.ods.api.jsonApi.JsonApiIdentifiable;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;

public class JsonApiResponse {

    private final UriInfo uriInfo;
    private Response.StatusType status;
    private JsonApiDocument jsonApiEntity;

    private JsonApiResponse(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public static RequiredStatus uriInfo(UriInfo uriInfo) {
        return new Builder(
                new JsonApiResponse(uriInfo));
    }


    public static class Builder implements RequiredStatus, RequiredEntity, RequiredEntityId, Buildable {

        private final JsonApiResponse instance;

        public Builder(JsonApiResponse instance) {
            this.instance = instance;
        }

        @Override
        public RequiredEntity ok() {
            instance.status = Response.Status.OK;
            return this;
        }

        @Override
        public RequiredEntityId created() {
            instance.status = Response.Status.CREATED;
            return this;
        }

        @Override
        public Buildable no_content() {
            instance.status = Response.Status.NO_CONTENT;
            return this;
        }

        @Override
        public Buildable entity(JsonApiIdentifiable entity) {
            instance.jsonApiEntity = new JsonApiDocument(entity, instance.uriInfo);
            return this;
        }

        @Override
        public Buildable entity(Collection<? extends JsonApiIdentifiable> entityCollection) {
            instance.jsonApiEntity = new JsonApiDocument(entityCollection, instance.uriInfo);
            return this;
        }

        @Override
        public Response build() {
            Response.ResponseBuilder jerseyBuilder = Response.status(instance.status);
            if(instance.jsonApiEntity != null) {
                jerseyBuilder
                        .type(JsonApiMediaType.JSONAPI)
                        .entity(instance.jsonApiEntity);
            }
            return jerseyBuilder.build();        }

        @Override
        public Buildable entityIdentifier(JsonApiIdentifiable entity) {
            instance.jsonApiEntity = new JsonApiDocument(entity, instance.uriInfo, true);
            return this;
        }
    }

    public interface RequiredStatus {
        RequiredEntity ok();
        RequiredEntityId created();
        Buildable no_content();
    }

    public interface RequiredEntity {
        Buildable entity(JsonApiIdentifiable entity);
        Buildable entity(Collection<? extends JsonApiIdentifiable> entity);
    }

    public interface RequiredEntityId {
        Buildable entityIdentifier(JsonApiIdentifiable entity);
    }

    public interface Buildable {
        Response build();
    }


}

