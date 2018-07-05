package org.jvalue.ods.rest.v2.jsonApi;


import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;

public class JsonApiResponse<T>  {

    private final UriInfo uriInfo;

    public JsonApiResponse(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }


    public JsonApiResponseWithDataBuilder<T> ok() {
        return new JsonApiResponseWithDataBuilder<T>(Response.Status.OK, uriInfo);
    }


    public JsonApiResponseWithDataBuilder<T> created() {
        return new JsonApiResponseWithDataBuilder<>(Response.Status.CREATED, uriInfo);
    }

    //do we need JsonApiResponses without content?
    public JsonApiResponseBuilder<T> no_content() {
        return new JsonApiResponseBuilder<T>(Response.Status.NO_CONTENT, uriInfo);
    }


    public class JsonApiResponseWithDataBuilder<R> {

        protected final Response.StatusType status;
        protected final UriInfo uriInfo;
        protected JsonApiDocument<T> data;

        private JsonApiResponseWithDataBuilder(Response.StatusType status, UriInfo uriInfo) {
            this.status = status;
            this.uriInfo = uriInfo;
        }


        public JsonApiResponseBuilder<T> data(T entity) {
            this.data = new JsonApiDocument<>(entity, uriInfo);
            return new JsonApiResponseBuilder<T>(status, uriInfo, data);
        }

        public JsonApiResponseBuilder<T> data(Collection<T> entityCollection) {
            this.data = new JsonApiDocument<>(entityCollection, uriInfo);
            return new JsonApiResponseBuilder<T>(status, uriInfo, data);
        }

    }

    public class JsonApiResponseBuilder<S> extends JsonApiResponseWithDataBuilder<S>{

        private JsonApiResponseBuilder(Response.StatusType status, UriInfo uriInfo) {
            super(status, uriInfo);
        }


        private JsonApiResponseBuilder(Response.StatusType status, UriInfo uriInfo, JsonApiDocument<T> data) {
            super(status, uriInfo);
            this.data = data;
        }


        public Response build() {

            Response.ResponseBuilder builder = Response.status(status);
            if(data != null) {
                builder.entity(data)
                        .type(JsonApiMediaType.JSONAPI);
            }
            return builder.build();

        }

    }

}
