package org.jvalue.ods.notifications.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.clients.DummyClient;


public final class ClientDatastoreTest {

	@Test
	public void testCachedStore() {

		ClientDatastore store = new CachedClientDatastore(new DummyClientDatastore());
		testCrud(store);

	}


	@Test
	public void testJsonDbStore() {

		ClientDatastore store = new JsonDbClientDatastore(DbFactory.createMockDbAccessor("dummy"));
		testCrud(store);

	}


	private void testCrud(ClientDatastore store) {

		Client[] clients = {
			new DummyClient("id1", "source"),
			new DummyClient("id1", "source"),
			new DummyClient("id2", "source")
		};

		for (Client client : clients) {
			assertFalse(store.contains(client.getClientId()));
			store.add(client);
			assertTrue(store.contains(client.getClientId()));
			assertTrue(store.getAll().contains(client));
			store.remove(client.getClientId());
			assertFalse(store.contains(client.getClientId()));
		}

		for (Client client : clients) store.add(client);
		assertEquals(store.getAll().size(), 2);

	}

}
