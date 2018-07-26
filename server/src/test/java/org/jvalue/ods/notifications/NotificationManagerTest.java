package org.jvalue.ods.notifications;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.db.couchdb.repositories.NotificationClientRepository;
import org.jvalue.ods.db.couchdb.RepositoryFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.notifications.sender.Sender;
import org.jvalue.ods.notifications.sender.SenderCache;
import org.jvalue.ods.notifications.sender.SenderResult;

import java.util.Arrays;


@RunWith(JMockit.class)
public final class NotificationManagerTest {

	@Mocked private SenderCache senderCache;
	@Mocked private Cache<GenericRepository<Client>> clientRepositoryCache;
	@Mocked private RepositoryFactory repositoryFactory;
	@Mocked private NotificationClientRepository clientRepository;
	@Mocked private Sender<HttpClient> httpSender;
	@Mocked private Sender<GcmClient> gcmSender;

	private final DataSource source = new DataSource("someId", null, null, null);
	private final HttpClient httpClient = new HttpClient("someId", "someCallbackUrl", false);
	private final GcmClient gcmClient = new GcmClient("someId", "someDeviceId");

	private NotificationManager notificationManager;

	@Before
	public void setupNotificationManager() {
		notificationManager = new NotificationManager(clientRepositoryCache, repositoryFactory, senderCache);
	}


	@Before
	public void setupSenderCache() {
		new Expectations() {{
			senderCache.get(source, gcmClient); result = gcmSender;
		}};
	}


	@Test
	public void testOnNewDataStart() {
		new Expectations(notificationManager) {{
			senderCache.get(source, httpClient); result = httpSender;
			notificationManager.getAll(source); result = Arrays.asList(httpClient, gcmClient);
		}};

		notificationManager.onNewDataStart(source);

		new Verifications() {{
			httpSender.onNewDataStart();
			gcmSender.onNewDataStart();
		}};
	}


	@Test
	public void testOnNewDataItem() {
		new Expectations(notificationManager) {{
			senderCache.get(source, httpClient); result = httpSender;
			notificationManager.getAll(source); result = Arrays.asList(httpClient, gcmClient);
		}};

		final ObjectNode data = new ObjectNode(JsonNodeFactory.instance);
		data.put("hello", "world");

		notificationManager.onNewDataItem(source, data);

		new Verifications() {{
			httpSender.onNewDataItem(data);
			gcmSender.onNewDataItem(data);
		}};
	}


	@Test
	public void testOnNewDataCompleteSuccess() {
		testOnNewDataCompleteHelper(SenderResult.Status.SUCCESS);
	}


	@Test
	public void testOnNewDataCompleteError() {
		testOnNewDataCompleteHelper(SenderResult.Status.ERROR);
	}


	@Test
	public void testOnNewDataCompleteRemove() {
		new Expectations(notificationManager) {{
			notificationManager.getAll(source); result = Arrays.asList(gcmClient);
			gcmSender.getSenderResult().getStatus(); result = SenderResult.Status.REMOVE_CLIENT;
			gcmSender.getSenderResult().getOldClient(); result = gcmClient;

			clientRepositoryCache.contains(anyString); result = true;
			clientRepositoryCache.get(anyString); result = clientRepository;
		}};

		notificationManager.onNewDataComplete(source);

		new Verifications() {{
			gcmSender.onNewDataComplete();
			senderCache.release(source, gcmClient);
			clientRepository.remove(gcmClient);
		}};
	}


	@Test
	public void testOnNewDataCompleteUpdate() {
		final GcmClient newGcmClient = new GcmClient("someOtherId", "someOtherDeviceId");
		new Expectations(notificationManager) {{
			notificationManager.getAll(source); result = Arrays.asList(gcmClient);
			gcmSender.getSenderResult().getStatus(); result = SenderResult.Status.UPDATE_CLIENT;
			gcmSender.getSenderResult().getOldClient(); result = gcmClient;
			gcmSender.getSenderResult().getNewClient(); result = newGcmClient;

			clientRepositoryCache.contains(anyString); result = true;
			clientRepositoryCache.get(anyString); result = clientRepository;
		}};

		notificationManager.onNewDataComplete(source);

		new Verifications() {{
			gcmSender.onNewDataComplete();
			senderCache.release(source, gcmClient);
			clientRepository.remove(gcmClient);
			clientRepository.add(newGcmClient);
		}};
	}


	private void testOnNewDataCompleteHelper(final SenderResult.Status status) {
		new Expectations(notificationManager) {{
			notificationManager.getAll(source); result = Arrays.asList(gcmClient);
			gcmSender.getSenderResult().getStatus(); result = status;

			result = new IllegalStateException("this method should not have been called");
		}};

		notificationManager.onNewDataComplete(source);

		new Verifications() {{
			gcmSender.onNewDataComplete();
			senderCache.get(source, gcmClient);
			senderCache.release(source, gcmClient);
		}};
	}

}
