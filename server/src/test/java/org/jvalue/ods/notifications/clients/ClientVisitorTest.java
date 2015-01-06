package org.jvalue.ods.notifications.clients; 

import org.junit.Test;
import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.api.notifications.HttpClient;

import static org.junit.Assert.assertEquals;


public final class ClientVisitorTest {

	@Test
	public final void testSimpleEcho() {

		DummyClientVisitor visitor = new DummyClientVisitor();

		GcmClient gcmClient = new GcmClient("", "");
		HttpClient httpClient = new HttpClient("", "", true);

		assertEquals(gcmClient.accept(visitor, "param"), "param");
		assertEquals(httpClient.accept(visitor, "param"), "param");

	}

}
