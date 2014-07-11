package org.jvalue.ods.notifications.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.clients.DummyClient;


public final class CachedClientDatastoreTest {

	@Test
	public void testCrud() {
		ClientDatastore store = new CachedClientDatastore(new DummyClientDatastore());

		Client[] clients = {
			new DummyClient("id1", "source"),
			new DummyClient("id1", "source"),
			new DummyClient("id2", "source")
		};

		for (Client client : clients) {
			store.add(client);
			assertTrue(store.contains(client));
			assertTrue(store.getAll().contains(client));
			store.remove(client);
			assertFalse(store.contains(client));
		}

		for (Client client : clients) store.add(client);
		assertEquals(store.getAll().size(), 2);

	}

}
