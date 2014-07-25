package org.jvalue.ods.notifications.clients; 

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;


public final class ClientTest {

	@Test
	public final void testEquals() {

		DummyClient client1 = new DummyClient("0", "source1");
		DummyClient client2 = new DummyClient("0", "source1");
		DummyClient client3 = new DummyClient("1", "source1");
		DummyClient client4 = new DummyClient("0", "source2");

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

		assertTrue(client1.equalsIgnoreId(client2));
		assertTrue(client1.equalsIgnoreId(client3));
		assertFalse(client1.equalsIgnoreId(client4));
		
	}


	@Test
	public final void testGet() {

		DummyClient client = new DummyClient("0", "source");
		assertEquals(client.getClientId(), "0");
		assertEquals(client.getSource(), "source");

	}


}
