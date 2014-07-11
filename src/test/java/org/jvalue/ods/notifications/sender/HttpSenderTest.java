package org.jvalue.ods.notifications.sender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DummyDataSource;
import org.jvalue.ods.data.generic.BaseObject;
import org.jvalue.ods.notifications.clients.HttpClient;
import org.jvalue.ods.utils.RestException;


public final class HttpSenderTest {

	private final DataSource source = DummyDataSource.newInstance("dummy", "dummy");
	private final HttpSender sender = new HttpSender();
	private final HttpClient 
		noDataClient = new HttpClient("dummy", "dummy", "dummy", "dummy", false),
		dataClient = new HttpClient("dummy", "dummy", "dummy", "dummy", true);


	@Test
	public final void testFailNoData() {
		
		SenderResult result = sender.notifySourceChanged(noDataClient, source, null);

		assertNotNull(result);
		assertEquals(result.getStatus(), SenderResult.Status.ERROR);
		assertTrue(result.getErrorCause() instanceof RestException);

	}


	@Test
	public final void testFail() {
		
		SenderResult result = sender.notifySourceChanged(
				dataClient, 
				source, 
				new BaseObject("hello"));

		assertNotNull(result);
		assertEquals(result.getStatus(), SenderResult.Status.ERROR);
		assertTrue(result.getErrorCause() instanceof RestException);

	}

}
