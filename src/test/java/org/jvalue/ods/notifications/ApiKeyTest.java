package org.jvalue.ods.notifications;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


public final class ApiKeyTest {

	@Test
	public final void testSimpleFetch() {
		ApiKey key = ApiKey.getInstance();
		assertTrue(key.toString() != null);
	}

}
