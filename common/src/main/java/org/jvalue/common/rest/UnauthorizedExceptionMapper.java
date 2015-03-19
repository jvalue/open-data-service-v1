package org.jvalue.common.rest;


import org.jvalue.ods.api.auth.UnauthorizedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public final class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

	@Override
	public Response toResponse(UnauthorizedException exception) {
		return RestUtils.createJsonFormattedException("not authorized", 401).getResponse();
	}

}
