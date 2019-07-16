/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v2.jsonapi.exceptionMapper;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Exception mapper mapping (de-)serializiation errors to JSONAPI formatted "bad request"-exceptions.
 */
public class JsonApiMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {

	@Override
	public Response toResponse(JsonMappingException e) {
		return JsonApiResponse
			.createExceptionResponse(Response.Status.BAD_REQUEST)
			.message(e.getMessage())
			.build();
	}
}
