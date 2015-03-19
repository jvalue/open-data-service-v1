package org.jvalue.common.rest;


import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public final class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

	@Override
	public Response toResponse(NotFoundException exception) {
		return RestUtils.createJsonFormattedException("nothing here", 404).getResponse();
	}

}
