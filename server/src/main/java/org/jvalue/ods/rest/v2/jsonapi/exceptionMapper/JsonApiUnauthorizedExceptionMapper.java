/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v2.jsonapi.exceptionMapper;

import org.jvalue.commons.auth.UnauthorizedException;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;


/**
 * Exception mapper mapping unauthorized-exceptions to JSONAPI format.
 */
public class JsonApiUnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {
	@Override
	public Response toResponse(UnauthorizedException e) {
		return JsonApiResponse
			.createExceptionResponse(Response.Status.UNAUTHORIZED)
			.build();

	}
}
