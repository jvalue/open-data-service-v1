package org.jvalue.ods.rest;


import org.ektorp.DocumentNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public final class DbExceptionMapper implements ExceptionMapper<DocumentNotFoundException> {

	@Override
	public Response toResponse(DocumentNotFoundException dnfe) {
		throw RestUtils.createNotFoundException();
	}

}
