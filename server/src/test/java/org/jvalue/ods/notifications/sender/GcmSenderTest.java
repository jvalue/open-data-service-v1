package org.jvalue.ods.notifications.sender;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.notifications.clients.GcmClient;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(JMockit.class)
public final class GcmSenderTest {

	@Test
	public final void testFail(
			@Mocked final DataSource source) {

		new Expectations() {{
			source.getId();
			result = "someSourceId";
		}};

		GcmSender sender = new GcmSender(GcmApiKeyHelper.getResourceName());
		GcmClient client = new GcmClient("dummy", "dummy");

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
