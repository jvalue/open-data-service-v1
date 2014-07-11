package org.jvalue.ods.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;


public final class RestExceptionTest {

	@Test
	public void testGet() {

		RestException re1 = new RestException(404);
		assertEquals(404, re1.getCode());
		assertNotNull(re1.getMessage());

		IOException cause = new IOException("error");
		RestException re2 = new RestException(cause);
		assertEquals(cause, re2.getCause());
		assertNotNull(re2.getMessage());

	}


}
