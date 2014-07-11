package org.jvalue.ods.notifications.sender;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;


public final class GcmApiKeyTest {

	@Before
	public final void setup() {
		GcmApiKeyHelper.setupKeyResource();
	}

	@Test
	public final void testSimpleFetch() {
		GcmApiKey key = GcmApiKey.getInstance();

		assertNotNull(key);
		assertNotNull(key.toString());
	}

}
