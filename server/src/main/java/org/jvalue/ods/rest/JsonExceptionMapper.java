package org.jvalue.ods.rest;


import com.fasterxml.jackson.databind.JsonMappingException;

import org.jvalue.common.rest.RestUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public final class JsonExceptionMapper implements ExceptionMapper<JsonMappingException> {

	@Override
	public Response toResponse(JsonMappingException jme) {
		return RestUtils.createJsonFormattedException(jme.getMessage(), 400).getResponse();
	}

}
