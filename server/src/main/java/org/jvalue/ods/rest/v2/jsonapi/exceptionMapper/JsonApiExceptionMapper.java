package org.jvalue.ods.rest.v2.jsonapi.exceptionMapper;


import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Generic exception mapper to map all exceptions that are not covered by the other exception mappers to JSONAPI format.
 */
public class JsonApiExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable throwable) {
		return JsonApiResponse
			.createExceptionResponse(Response.Status.INTERNAL_SERVER_ERROR)
			.build();
	}
}
