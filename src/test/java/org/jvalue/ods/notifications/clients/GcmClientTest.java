package org.jvalue.ods.notifications.clients; 

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;


public final class GcmClientTest {

	@Test
	public final void testEquals() {

		GcmClient client1 = new GcmClient("0", "source1");
		GcmClient client2 = new GcmClient("0", "source1");
		GcmClient client3 = new GcmClient("1", "source1");
		GcmClient client4 = new GcmClient("0", "source2");

		assertEquals(client1, client1);
		assertEquals(client1, client2);
		assertNotEquals(client1, client3);
		assertNotEquals(client1, client4);
		assertNotEquals(client1, null);
		assertNotEquals(client1, new Object());

		assertEquals(client1.hashCode(), client1.hashCode());
		assertEquals(client1.hashCode(), client2.hashCode());
		assertNotEquals(client1.hashCode(), client3.hashCode());
		assertNotEquals(client1.hashCode(), client4.hashCode());
		
	}


	@Test
	public final void testGet() {

		GcmClient client = new GcmClient("0", "source");
		assertEquals(client.getId(), "0");
		assertEquals(client.getSource(), "source");

	}



}
