package org.jvalue.ods.utils;

import org.junit.Test;

import java.io.IOException;

import javax.ws.rs.core.Response.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public final class RestExceptionTest {

	@Test
	public void testGet() {

		RestException re1 = new RestException(Status.fromStatusCode(404));
		assertEquals(Status.NOT_FOUND, re1.getStatus());
		assertNotNull(re1.getMessage());

		IOException cause = new IOException("error");
		RestException re2 = new RestException(cause);
		assertEquals(cause, re2.getCause());
		assertNotNull(re2.getMessage());
		assertNull(re2.getStatus());

	}


}
