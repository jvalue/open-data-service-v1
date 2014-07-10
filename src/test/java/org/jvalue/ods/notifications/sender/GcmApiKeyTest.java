package org.jvalue.ods.notifications.sender;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


public final class GcmApiKeyTest {

	@Test
	public final void testSimpleFetch() {
		GcmApiKey.setKeyResourceName("/googleApi.key.template");
		GcmApiKey key = GcmApiKey.getInstance();
		assertTrue(key.toString() != null);
	}

}
