package org.jvalue.ods.rest.v2.jsonapi.exceptionHandler;

import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        return JsonApiResponse
                .createExceptionResponse(Response.Status.NOT_FOUND.getStatusCode())
                .build();
    }
}
