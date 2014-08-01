package org.jvalue.ods.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;
import org.restlet.data.Status;


public final class RestExceptionTest {

	@Test
	public void testGet() {

		RestException re1 = new RestException(Status.valueOf(404));
		assertEquals(Status.CLIENT_ERROR_NOT_FOUND, re1.getStatus());
		assertNotNull(re1.getMessage());

		IOException cause = new IOException("error");
		RestException re2 = new RestException(cause);
		assertEquals(cause, re2.getCause());
		assertNotNull(re2.getMessage());
		assertNull(re2.getStatus());

	}


}
