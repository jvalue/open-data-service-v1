package org.jvalue.ods.notifications.clients; 

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public final class ClientVisitorTest {

	@Test
	public final void testSimpleEcho() {

		DummyClientVisitor visitor = new DummyClientVisitor();

		GcmClient gcmClient = new GcmClient("", "");
		HttpClient httpClient = new HttpClient("", "", "", "", true);

		assertEquals(gcmClient.accept(visitor, "param"), "param");
		assertEquals(httpClient.accept(visitor, "param"), "param");

	}

}
