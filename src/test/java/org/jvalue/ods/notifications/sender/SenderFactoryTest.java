package org.jvalue.ods.notifications.sender;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public final class SenderFactoryTest {

	@Test
	public void testGet() {

		try {
			Sender<?> sender1 = SenderFactory.getGcmSender();
			assertNotNull(sender1);
			assertTrue(sender1 == SenderFactory.getGcmSender());
		} catch (Exception e) {
			assertFalse(GcmApiKeyHelper.isApiKeyPresent());
		}

		Sender<?> sender2 = SenderFactory.getRestSender();
		assertNotNull(sender2);
		assertTrue(sender2 == SenderFactory.getRestSender());
	}

}
