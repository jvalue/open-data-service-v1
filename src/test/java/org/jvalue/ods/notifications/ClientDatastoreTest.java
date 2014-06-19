package org.jvalue.ods.notifications;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public final class ClientDatastoreTest {

	@BeforeClass
	@AfterClass
	public static final void clearDatabase() {
		ClientDatastoreFactory.getCouchDbClientDatastore().removeAllClients();
	}


	@Test
	public final void testRegisterUnregister() {
		ClientDatastore store = ClientDatastoreFactory.getCouchDbClientDatastore();

		List<String> clients = new ArrayList<String>();
		clients.add("foo");
		clients.add("bar");
		clients.add("bar");
		clients.add("foobar");
		List<String> sources = new ArrayList<String>();
		sources.add("pegelonline");
		sources.add("pegelonline");
		sources.add("pegeloffline");
		sources.add("nopegel");

		for (int i = 0; i < clients.size(); i++) {
			String client = clients.get(i);
			String source = sources.get(i);

			assertTrue(!store.isClientRegistered(client, source));
			assertTrue(store.getRegisteredClients().get(source) == null 
					|| !store.getRegisteredClients().get(source).contains(client));

			store.registerClient(client, source);

			assertTrue(store.isClientRegistered(client, source));
			assertTrue(store.getRegisteredClients().get(source).contains(client));

			store.unregisterClient(client, source);

			assertTrue(!store.isClientRegistered(client, source));
			assertTrue(store.getRegisteredClients().get(source) == null 
					|| !store.getRegisteredClients().get(source).contains(client));
		}
	}


	@Test
	public final void testUpdateClientId() {
		String source = "pegelonline";
		String client1 = "foo";
		String client2 = "bar";

		ClientDatastore store = ClientDatastoreFactory.getCouchDbClientDatastore();

		assertTrue(!store.isClientRegistered(client1, source));
		assertTrue(!store.isClientRegistered(client2, source));

		store.registerClient(client1, source);

		assertTrue(store.isClientRegistered(client1, source));
		assertTrue(!store.isClientRegistered(client2, source));

		store.updateClientId(client1, client2);

		assertTrue(!store.isClientRegistered(client1, source));
		assertTrue(store.isClientRegistered(client2, source));

		store.unregisterClient(client2, source);

		assertTrue(!store.isClientRegistered(client1, source));
		assertTrue(!store.isClientRegistered(client2, source));
	}

}
