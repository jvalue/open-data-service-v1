package org.jvalue.ods.notifications.sender;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;


public final class GcmApiKeyTest {

	@Test
	public final void testSuccess() {

		GcmApiKey key = new GcmApiKey(GcmApiKeyHelper.getResourceName());
		assertNotNull(key.toString());

	}


	@Test(expected = IllegalArgumentException.class)
	public final void testFailure() {

		new GcmApiKey("foobar");

	}

}
