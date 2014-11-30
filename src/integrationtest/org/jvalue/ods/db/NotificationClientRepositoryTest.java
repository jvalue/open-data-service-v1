package org.jvalue.ods.db;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.ektorp.CouchDbInstance;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.clients.ClientVisitor;

import java.util.List;

public class NotificationClientRepositoryTest extends AbstractDbTest {

	private NotificationClientRepository clientRepository;

	public NotificationClientRepositoryTest() {
		super("test_notification_clients");
	}


	@Override
	protected void createDatabase(CouchDbInstance couchDbInstance, String databaseName) {
		clientRepository = new NotificationClientRepository(couchDbInstance.createConnector(databaseName, true));
	}


	@Test
	public void testCustomViews() {
		Client client1 = new DummyClient("id1", "source1");
		Client client2 = new DummyClient("id2", "source2");

		clientRepository.add(client1);
		Assert.assertEquals(client1, clientRepository.get(client1.getId()));

		clientRepository.add(client2);
		Assert.assertEquals(client2, clientRepository.get(client2.getId()));

		List<Client> clients = clientRepository.getAll();
		Assert.assertTrue(clients.contains(client1) && clients.contains(client2));

		clients = clientRepository.findByClientId("id1");
		Assert.assertTrue(clients.contains(client1) && !clients.contains(client2));

		clients = clientRepository.findBySource("source2");
		Assert.assertTrue(!clients.contains(client1) && clients.contains(client2));
	}


	public static final class DummyClient extends Client {

		@JsonCreator
		public DummyClient(
				@JsonProperty("clientId") String clientId,
				@JsonProperty("source") String source) {
			super(clientId, source);
		}

		public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
			return null;
		}

	}

}
