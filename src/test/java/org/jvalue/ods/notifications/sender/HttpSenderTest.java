package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.notifications.clients.HttpClient;
import org.jvalue.ods.utils.RestException;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(JMockit.class)
public final class HttpSenderTest {

	@Mocked
	private DataSource source;
	private final HttpSender sender = new HttpSender();
	private final HttpClient 
		noDataClient = new HttpClient("dummy", "dummy", "dummy", false),
		dataClient = new HttpClient("dummy", "dummy", "dummy", true);


	@Test
	public final void testFailNoData() {
		setupSource();

		SenderResult result = sender.notifySourceChanged(noDataClient, source, null);
		assertNotNull(result);
		assertEquals(result.getStatus(), SenderResult.Status.ERROR);
		assertTrue(result.getErrorCause() instanceof RestException);
	}


	@Test
	public final void testFail() {
		setupSource();

		SenderResult result = sender.notifySourceChanged(
				dataClient, 
				source, 
				new ArrayNode(JsonNodeFactory.instance));
		assertNotNull(result);
		assertEquals(result.getStatus(), SenderResult.Status.ERROR);
		assertTrue(result.getErrorCause() instanceof RestException);
	}


	private void setupSource() {
		new Expectations() {{
			source.getSourceId();
			result = "someSourceId";
		}};
	}

}
