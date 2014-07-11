package integration.org.jvalue.ods.notifications.db;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.notifications.db.ClientDatastore;
import org.jvalue.ods.notifications.db.ClientDatastoreFactory;


public final class ClientDatastoreTest {

	@BeforeClass
	@AfterClass
	public static final void clearDatabase() {
		ClientDatastore store = ClientDatastoreFactory.getClientDatastore();
		for (Client client : store.getAll()) {
			store.remove(client);
		}
	}


	@Test
	public final void testRegisterUnregister() {
		ClientDatastore store = ClientDatastoreFactory.getClientDatastore();

		List<Client> clients = new ArrayList<Client>();
		clients.add(new GcmClient("foo", "pegelonline"));
		clients.add(new GcmClient("bar", "pegelonline"));
		clients.add(new GcmClient("bar", "pegeloffline"));
		clients.add(new GcmClient("foobar", "nopegel"));

		for (Client client : clients) {
			assertTrue(!store.contains(client));
			assertTrue(!store.getAll().contains(client));

			store.add(client);

			assertTrue(store.contains(client));
			assertTrue(store.getAll().contains(client));

			store.remove(client);

			assertTrue(!store.contains(client));
			assertTrue(!store.getAll().contains(client));
		}
	}
}
