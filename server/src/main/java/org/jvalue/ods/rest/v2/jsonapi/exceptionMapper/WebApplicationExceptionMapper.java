package org.jvalue.ods.rest.v2.jsonapi.exceptionMapper;

import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;


/**
 * Exception mapper to map web application exceptions to JSONAPI format.
 */
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
	@Override
	public Response toResponse(WebApplicationException e) {
		return JsonApiResponse
			.createExceptionResponse(Response.Status.fromStatusCode(e.getResponse().getStatus()))
			.message(e.getMessage())
			.build();
	}
}
