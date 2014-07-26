package org.jvalue.ods.notifications.clients; 

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public final class ClientFactoryTest {

	@Test
	public final void testCreateGcmClient() {

		GcmClient client = ClientFactory.newGcmClient("source", "id");
		assertEquals("source", client.getSource());
		assertEquals("id", client.getGcmClientId());

	}


	@Test
	public final void testCreateHttpClient() {

		HttpClient client = ClientFactory.newHttpClient("source", "url", "param", true);
		assertEquals("source", client.getSource());
		assertEquals("url", client.getRestUrl());
		assertEquals("param", client.getSourceParam());
		assertEquals(true, client.getSendData());

	}

}
