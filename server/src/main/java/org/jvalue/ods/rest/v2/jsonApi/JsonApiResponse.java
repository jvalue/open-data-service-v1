package org.jvalue.ods.rest.v2.jsonApi;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;

public class JsonApiResponse<T> {

    private UriInfo uriInfo;
    private Response.StatusType status;
    private JsonApiDocument<T> jsonApiEntity;

    public JsonApiResponse() {
    }

    public RequiredStatus<T> uriInfo(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
        return new Builder<>(this);
    }


    public class Builder<S> implements RequiredStatus<S>, RequiredEntity<S>, RequiredEntityId<S>, Buildable {

        private final JsonApiResponse<S> instance;

        public Builder(JsonApiResponse<S> instance) {
            this.instance = instance;
        }

        @Override
        public RequiredEntity<S> ok() {
            instance.status = Response.Status.OK;
            return this;
        }

        @Override
        public RequiredEntityId<S> created() {
            instance.status = Response.Status.CREATED;
            return this;
        }

        @Override
        public Buildable no_content() {
            instance.status = Response.Status.NO_CONTENT;
            return this;
        }

        @Override
        public Buildable entity(S entity) {
            instance.jsonApiEntity = new JsonApiDocument<>(entity, uriInfo);
            return this;
        }

        @Override
        public Buildable entity(Collection<S> entityCollection) {
            instance.jsonApiEntity = new JsonApiDocument<>(entityCollection, uriInfo);
            return this;
        }

        @Override
        public Response build() {
            Response.ResponseBuilder jerseyBuilder = Response.status(status);
            if(instance.jsonApiEntity != null) {
                jerseyBuilder
                        .type(JsonApiMediaType.JSONAPI)
                        .entity(instance.jsonApiEntity);
            }
            return jerseyBuilder.build();        }

        @Override
        public Buildable entityIdentifier(S entity) {
            instance.jsonApiEntity = new JsonApiDocument<>(entity, uriInfo, true);
            return this;
        }
    }

    public interface RequiredStatus<T> {
        RequiredEntity<T> ok();
        RequiredEntityId<T> created();
        Buildable no_content();
    }

    public interface RequiredEntity<T> {
        Buildable entity(T entity);
        Buildable entity(Collection<T> entity);
    }

    public interface RequiredEntityId<T> {
        Buildable entityIdentifier(T entity);
    }

    public interface Buildable {
        Response build();
    }


}

