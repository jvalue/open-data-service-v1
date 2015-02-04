package org.jvalue.common.rest;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestUtils {

	private RestUtils() { }


	public static WebApplicationException createNotFoundException() {
		return createJsonFormattedException("not found", 404);
	}

	public static WebApplicationException createJsonFormattedException(String message, int status) {
		ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
		node.put("message", message);
		node.put("status", status);
		return new WebApplicationException(
				Response
						.status(status)
						.entity(node.toString())
						.type(MediaType.APPLICATION_JSON)
						.build());

	}

}
