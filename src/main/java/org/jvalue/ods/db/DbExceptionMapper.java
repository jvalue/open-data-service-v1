package org.jvalue.ods.db;


import org.ektorp.DocumentNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public final class DbExceptionMapper implements ExceptionMapper<DocumentNotFoundException> {

	@Override
	public Response toResponse(DocumentNotFoundException dnfe) {
		return Response.status(404).entity("not found").type(MediaType.TEXT_PLAIN).build();
	}

}
