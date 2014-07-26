package org.jvalue.ods.server.utils; 

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.restlet.data.Status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;


public final class RestletResultTest {

	@Test
	public final void testEmptySuccess() {
		RestletResult result = RestletResult.newSuccessResult();
		assertEquals(Status.SUCCESS_NO_CONTENT, result.getStatus());
		assertNull(result.getData());
	}


	@Test
	public final void testSuccess() {
		JsonNode json = new TextNode("dummy");
		RestletResult result = RestletResult.newSuccessResult(json);
		assertEquals(Status.SUCCESS_OK, result.getStatus());
		assertEquals(json, result.getData());
	}


	@Test
	public final void testSuccessCustomStatus() {
		JsonNode json = new TextNode("dummy");
		RestletResult result = RestletResult.newSuccessResult(Status.SUCCESS_ACCEPTED, json);
		assertEquals(Status.SUCCESS_ACCEPTED, result.getStatus());
		assertEquals(json, result.getData());
	}


	@Test
	public final void testError() {
		RestletResult result = RestletResult.newErrorResult(
				Status.CLIENT_ERROR_BAD_REQUEST, 
				"dummy");
		assertEquals(Status.CLIENT_ERROR_BAD_REQUEST, result.getStatus());
		assertNotNull(result.getData());
	}

}
