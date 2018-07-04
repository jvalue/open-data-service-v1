package org.jvalue.ods.rest.v2.jsonApi;


import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;

public class JsonApiResponse<T>  {

    public StatusContainer<T> status(Response.StatusType status) {
        return new StatusContainer<>(status);
    }


    public StatusContainer<T> ok() {
        return new StatusContainer<>(Response.Status.OK);
    }


    public class StatusContainer<T> {

        private final Response.StatusType status;

        private StatusContainer(Response.StatusType status) {
            this.status = status;
        }


        public JsonApiResponseBuilder<T> path(UriInfo uriInfo) {
            return new JsonApiResponseBuilder<>(status, uriInfo);
        }
    }

    public class JsonApiResponseBuilder<T>{

        private final Response.StatusType status;
        private final UriInfo uriInfo;
        private Optional<JsonApiDocument> entity = Optional.empty();


        public JsonApiResponseBuilder(Response.StatusType status, UriInfo uriInfo) {
            this.status = status;
            this.uriInfo = uriInfo;
        }


        public JsonApiResponseBuilder<T> entity(T entity) {
            //todo: use existing JsonApiDocument for consequent calls instead of recreating it each time?
            this.entity = Optional.of(new JsonApiDocument<>(entity, uriInfo));
            return this;
        }


        public JsonApiResponseBuilder<T> entity(Collection<T> entity) {
            this.entity = Optional.of(new JsonApiDocument<>(entity, uriInfo));
            return this;
        }


        //todo: use UUID generator instead?
        public JsonApiResponseBuilder<T> entity(T entity,
                                                String id) {
            this.entity = Optional.of(new JsonApiDocument<>(entity, uriInfo, id));
            return this;
        }


        public Response build() {

            Response.ResponseBuilder builder = Response.status(status);
            entity.ifPresent(e -> builder.entity(e));
            return builder.build();
        }

    }

}
