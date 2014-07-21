package org.jvalue.ods.notifications;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DummyDataSource;
import org.jvalue.ods.data.generic.BaseObject;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.clients.DummyClient;
import org.jvalue.ods.notifications.db.DummyClientDatastore;
import org.jvalue.ods.notifications.definitions.NotificationDefinition;
import org.jvalue.ods.notifications.rest.RestAdapter;
import org.jvalue.ods.notifications.sender.NotificationSender;
import org.jvalue.ods.notifications.sender.SenderResult;
import org.restlet.Request;


public final class NotificationManagerTest {

	private NotificationManager manager;
	private Client client;
	private DataSource source;
	private GenericEntity data;

	private int notifyCalledCount;


	@Before
	public final void setup() {

		manager = new NotificationManager(new DummyClientDatastore());
		client = new DummyClient("dummy", "dummy");
		source = DummyDataSource.newInstance("dummy", "dummy");
		data = new BaseObject("dummy");

	}


	@Test
	public final void testSourceChanged() {

		assertNotNull(manager.getNotificationDefinitions());
		assertEquals(manager.getNotificationDefinitions().size(), 0);

		manager.addDefinition(DummyClient.class, new DummyNotificationDefinition(
					new NotificationSender<DummyClient>() {

						@Override
						public SenderResult notifySourceChanged(
								DummyClient client,
								DataSource source,
								GenericEntity data) {

							assertNotNull(client);
							assertNotNull(source);
							assertNotNull(data);

							notifyCalledCount++;

							return getSuccessResult();
						}
					}));

		assertNotNull(manager.getNotificationDefinitions());
		assertEquals(manager.getNotificationDefinitions().size(), 1);

		manager.notifySourceChanged(source, data);
		assertEquals(notifyCalledCount, 0);

		assertNotNull(manager.getAllClients());
		assertEquals(manager.getAllClients().size(), 0);
		manager.registerClient(client);
		assertEquals(manager.getAllClients().size(), 1);
		assertTrue(manager.getAllClients().contains(client));
		manager.unregisterClient(client.getClientId());
		assertEquals(manager.getAllClients().size(), 0);

		manager.registerClient(client);
		manager.notifySourceChanged(source, data);
		assertEquals(notifyCalledCount, 1);
		manager.notifySourceChanged(source, data);
		assertEquals(notifyCalledCount, 2);

	}


	@Test
	public final void testSuccessSenderResult() { 

		setupSenderResultTest(
				new NotificationSender<DummyClient>() {

					@Override
					public SenderResult notifySourceChanged(
							DummyClient client,
							DataSource source,
							GenericEntity data) {

						return getSuccessResult();
					}
				});

		assertEquals(manager.getAllClients().size(), 1);
		assertTrue(manager.getAllClients().contains(client));
	}


	@Test
	public final void testErrorSenderResult() { 

		setupSenderResultTest(
				new NotificationSender<DummyClient>() {

					@Override
					public SenderResult notifySourceChanged(
							DummyClient client,
							DataSource source,
							GenericEntity data) {

						return getErrorResult("error");
					}
				});

		assertEquals(manager.getAllClients().size(), 1);
		assertTrue(manager.getAllClients().contains(client));

	}


	@Test
	public final void testRemoveClientSenderResult() { 

		setupSenderResultTest(
				new NotificationSender<DummyClient>() {

					@Override
					public SenderResult notifySourceChanged(
							DummyClient client,
							DataSource source,
							GenericEntity data) {

						return getRemoveClientResult(client);
					}
				});

		assertEquals(manager.getAllClients().size(), 0);

	}


	@Test
	public final void testUpdateClientSenderResult() { 

		final Client newClient = new DummyClient("newDummy", "newDummy");

		setupSenderResultTest(
				new NotificationSender<DummyClient>() {

					@Override
					public SenderResult notifySourceChanged(
							DummyClient client,
							DataSource source,
							GenericEntity data) {

						return getUpdateClientResult(client, newClient);
					}
				});

		assertEquals(manager.getAllClients().size(), 1);
		assertTrue(manager.getAllClients().contains(newClient));
		assertNotEquals(client, newClient);
	}


	private void setupSenderResultTest(NotificationSender<DummyClient> sender) {

		manager.addDefinition(DummyClient.class, new DummyNotificationDefinition(sender));
		manager.registerClient(client);
		manager.notifySourceChanged(source, data);

	}


	static final class DummyNotificationDefinition implements NotificationDefinition<DummyClient> {

		private final NotificationSender<DummyClient> sender;

		public DummyNotificationDefinition(NotificationSender<DummyClient> sender) {
			this.sender = sender;
		}

		@Override
		public String getRestName() {
			return "/rest";
		}

		@Override
		public RestAdapter<DummyClient> getRestAdapter() {
			return new RestAdapter<DummyClient>() {

				@Override
				protected DummyClient toClient(Request request, String source) {
					return null;
				}

				@Override
				protected void getParameters(Set<String> params) { }

			};

		}


		@Override
		public NotificationSender<DummyClient> getNotificationSender() {
			return sender;
		}

	}

}
