package org.jvalue.ods.api.notifications;


import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.AbstractApiTest;

import java.util.List;

import retrofit.RetrofitError;

public final class NotificationApiTest extends AbstractApiTest {

	private final HttpClient httpClient = new HttpClient("someHttpId", "someCallbackUrl", false);
	private final GcmClient gcmClient = new GcmClient("someGcmId", "someDeviceId");

	private final ClientDescription
			httpClientDescription = new HttpClientDescription(httpClient.getCallbackUrl(), httpClient.getSendData()),
			gcmClientDescription = new GcmClientDescription(gcmClient.getGcmClientId());

	@Test
	public void testCrud() {
		testClient(httpClient, httpClientDescription);
		testClient(gcmClient, gcmClientDescription);

		notificationApi.register(sourceId, httpClient.getId(), httpClientDescription);
		notificationApi.register(sourceId, gcmClient.getId(), gcmClientDescription);
		List<Client> clients = notificationApi.getAll(sourceId);

		Assert.assertEquals(2, clients.size());
		Assert.assertTrue(clients.contains(httpClient));
		Assert.assertTrue(clients.contains(gcmClient));
	}


	private void testClient(Client client, ClientDescription clientDescription) {
		notificationApi.register(sourceId, client.getId(), clientDescription);
		Assert.assertEquals(client, notificationApi.get(sourceId, client.getId()));
		notificationApi.unregister(sourceId, client.getId());
		try {
			notificationApi.get(sourceId, client.getId());
		} catch (RetrofitError re) {
			return;
		}
		Assert.fail();
	}

}
