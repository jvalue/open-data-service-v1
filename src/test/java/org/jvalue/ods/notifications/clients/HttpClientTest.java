package org.jvalue.ods.notifications.clients; 

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;


public final class HttpClientTest {

	@Test
	public final void testEquals() {

		HttpClient client1 = new HttpClient("0", "source1", "url1", "param1", true);
		HttpClient client2 = new HttpClient("0", "source1", "url1", "param1", true);
		HttpClient client3 = new HttpClient("0", "source1", "url2", "param1", true);
		HttpClient client4 = new HttpClient("0", "source1", "url1", "param1", false);
		HttpClient client5 = new HttpClient("0", "source2", "url1", "param1", true);

		assertEquals(client1, client1);
		assertEquals(client1, client2);
		assertNotEquals(client1, client3);
		assertNotEquals(client1, client4);
		assertNotEquals(client1, client5);
		assertNotEquals(client1, null);
		assertNotEquals(client1, new Object());

		assertEquals(client1.hashCode(), client1.hashCode());
		assertEquals(client1.hashCode(), client2.hashCode());
		assertNotEquals(client1.hashCode(), client3.hashCode());
		assertNotEquals(client1.hashCode(), client4.hashCode());
		assertNotEquals(client1.hashCode(), client5.hashCode());
		
	}


	@Test
	public final void testGet() {

		HttpClient client = new HttpClient("0", "source", "url", "param", true);
		assertEquals(client.getId(), "0");
		assertEquals(client.getSource(), "source");
		assertEquals(client.getRestUrl(), "url");
		assertEquals(client.getSourceParam(), "param");
		assertEquals(client.getSendData(), true);

	}



}
