package org.jvalue.common.rest;


import org.ektorp.DocumentNotFoundException;
import org.jvalue.common.rest.RestUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public final class DbExceptionMapper implements ExceptionMapper<DocumentNotFoundException> {

	@Override
	public Response toResponse(DocumentNotFoundException dnfe) {
		return RestUtils.createNotFoundException().getResponse();
	}

}
