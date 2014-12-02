package org.jvalue.ods.notifications.sender;

import org.junit.Test;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DummyDataSource;
import org.jvalue.ods.notifications.clients.GcmClient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public final class GcmSenderTest {

	@Test
	public final void testFail() {
		
		GcmSender sender = new GcmSender(GcmApiKeyHelper.getResourceName());
		GcmClient client = new GcmClient("dummy", "dummy");
		DataSource source = DummyDataSource.newInstance("dummy", "dummy");

		try {
			SenderResult result = sender.notifySourceChanged(client, source, null);

			assertNotNull(result);
			assertEquals(result.getStatus(), SenderResult.Status.ERROR);
			assertTrue(result.getErrorMsg() != null || result.getErrorCause() != null);

		} catch (IllegalArgumentException iae) {
			// sender does not work when apikey is not set
			assertFalse(GcmApiKeyHelper.isApiKeyPresent());
		}

	}

}
