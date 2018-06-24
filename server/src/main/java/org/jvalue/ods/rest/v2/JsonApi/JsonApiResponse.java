package org.jvalue.ods.rest.v2.JsonApi;


import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;

public class JsonApiResponse<T>  {
    public static final String JSONAPITYPE = "application/vnd.api+json";

    public JsonApiResponse() {
    }

    public JsonApiResponseBuilder<T> status(Response.StatusType status) {
        return new JsonApiResponseBuilder<>(status);
    }

    public JsonApiResponseBuilder<T> ok() {
        return new JsonApiResponseBuilder<>(Response.Status.OK);
    }

    public class JsonApiResponseBuilder<T>{
        private final Response.StatusType status;
        private URI path;
        private Optional<JsonApiDocument> entity = Optional.empty();

        @Context UriInfo uriInfo;

        private JsonApiResponseBuilder(Response.StatusType status) {
            this.status = status;
        }

        public JsonApiResponseBuilder<T> path(URI path) {
            this.path = path;
            return this;
        }

        public JsonApiResponseBuilder<T> entity(T entity) {
            //maybe use existing JsonApiDocument for consequent calls instead of recreating it each time?
            //assert path has been instantiated. how can i make sure path has been called first?
            this.entity = Optional.of(new JsonApiDocument<>(entity, path));
            return this;
        }

        public JsonApiResponseBuilder<T> entity(Collection<T> entity) {
            //assert bla
            this.entity = Optional.of(new JsonApiDocument<>(entity, path));
            return this;
        }

        //maybe use UUID generator instead?
        public JsonApiResponseBuilder<T> entity(T entity, String id) {

            this.entity = Optional.of(new JsonApiDocument<>(entity, path, id));
            return this;
        }

        public Response build() {
            Response.ResponseBuilder builder = Response.status(status).type(JSONAPITYPE);
            if(entity.isPresent()) {
                builder.entity(entity.get());
            }
            return builder.build();
        }

    }

}
