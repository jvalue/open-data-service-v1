package org.jvalue.ods.db;


import org.ektorp.CouchDbInstance;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.clients.GcmClient;

import java.util.List;

public class NotificationClientRepositoryTest extends AbstractDbTest {

	private NotificationClientRepository clientRepository;

	public NotificationClientRepositoryTest() {
		super(NotificationClientRepositoryTest.class.getSimpleName());
	}


	@Override
	protected void createDatabase(CouchDbInstance couchDbInstance, String databaseName) {
		clientRepository = new NotificationClientRepository(couchDbInstance, NotificationClientRepositoryTest.class.getSimpleName());
	}


	@Test
	public void testCustomViews() {
		Client client1 = new GcmClient("id1", "someGcmId1");
		Client client2 = new GcmClient("id2", "someGcmId2");

		clientRepository.add(client1);
		Assert.assertEquals(client1, clientRepository.findById(client1.getClientId()));

		clientRepository.add(client2);
		Assert.assertEquals(client2, clientRepository.findById(client2.getClientId()));

		List<Client> clients = clientRepository.getAll();
		Assert.assertTrue(clients.contains(client1) && clients.contains(client2));

		Client client = clientRepository.findById("id1");
		Assert.assertNotNull(client);
	}

}
