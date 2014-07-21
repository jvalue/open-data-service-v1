package integration.org.jvalue.ods.notifications.db;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.clients.ClientFactory;
import org.jvalue.ods.notifications.db.ClientDatastore;
import org.jvalue.ods.notifications.db.ClientDatastoreFactory;


public final class ClientDatastoreTest {

	@BeforeClass
	@AfterClass
	public static final void clearDatabase() {
		ClientDatastore store = ClientDatastoreFactory.getClientDatastore();
		for (Client client : store.getAll()) {
			store.remove(client.getClientId());
		}
	}


	@Test
	public final void testRegisterUnregister() {
		ClientDatastore store = ClientDatastoreFactory.getClientDatastore();

		List<Client> clients = new ArrayList<Client>();
		clients.add(ClientFactory.newGcmClient("pegelonline", "foo"));
		clients.add(ClientFactory.newGcmClient("pegelonline", "bar"));
		clients.add(ClientFactory.newGcmClient("pegeloffline", "bar"));
		clients.add(ClientFactory.newGcmClient("nopegel", "foobar"));

		for (Client client : clients) {
			assertTrue(!store.contains(client.getClientId()));
			assertTrue(!store.getAll().contains(client));

			store.add(client);

			assertTrue(store.contains(client.getClientId()));
			assertTrue(store.getAll().contains(client));

			store.remove(client.getClientId());

			assertTrue(!store.contains(client.getClientId()));
			assertTrue(!store.getAll().contains(client));
		}
	}
}
