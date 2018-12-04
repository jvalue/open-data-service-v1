package org.jvalue.ods.rest.v2.jsonapi.exceptionMapper;

import org.ektorp.DocumentNotFoundException;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Exception mapper mapping database exceptions to JSONAPI formatted "not found"-exceptions.
 */
public class JsonApiDbExceptionMapper implements ExceptionMapper<DocumentNotFoundException> {
	@Override
	public Response toResponse(DocumentNotFoundException e) {
		return JsonApiResponse
			.createExceptionResponse(Response.Status.NOT_FOUND)
			.build();
	}
}
