package org.jvalue.ods.notifications;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.NotificationClientRepository;
import org.jvalue.ods.db.RepositoryFactory;
import org.jvalue.ods.notifications.sender.Sender;
import org.jvalue.ods.notifications.sender.SenderCache;
import org.jvalue.ods.notifications.sender.SenderResult;

import java.util.Arrays;

import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class NotificationManagerTest {

	@Mocked private SenderCache senderCache;
	@Mocked private Cache<NotificationClientRepository> clientRepositoryCache;
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
		new NonStrictExpectations() {{
			senderCache.get(source, httpClient); result = httpSender;
			senderCache.get(source, gcmClient); result = gcmSender;
		}};
	}


	@Test
	public void testOnNewDataStart() {
		new Expectations(notificationManager) {{
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
		new NonStrictExpectations(notificationManager) {{
			notificationManager.getAll(source); result = Arrays.asList(gcmClient);
			gcmSender.getSenderResult().getStatus(); result = status;

			notificationManager.remove((DataSource) any, (DataRepository) any, (Client) any);
			result = new IllegalStateException("this method should not have been called");
		}};

		notificationManager.onNewDataComplete(source);

		new Verifications() {{
			gcmSender.onNewDataComplete();
			senderCache.release(source, gcmClient);
		}};
	}

}
