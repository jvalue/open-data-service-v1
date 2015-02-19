package org.jvalue.ods.api;


import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.notifications.ClientDescription;
import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.api.notifications.GcmClientDescription;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.notifications.HttpClientDescription;

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

		notificationApi.registerClient(sourceId, httpClient.getId(), httpClientDescription);
		notificationApi.registerClient(sourceId, gcmClient.getId(), gcmClientDescription);
		List<Client> clients = notificationApi.getAllClients(sourceId);

		Assert.assertEquals(2, clients.size());
		Assert.assertTrue(clients.contains(httpClient));
		Assert.assertTrue(clients.contains(gcmClient));
	}


	private void testClient(Client client, ClientDescription clientDescription) {
		notificationApi.registerClient(sourceId, client.getId(), clientDescription);
		Assert.assertEquals(client, notificationApi.getClient(sourceId, client.getId()));
		notificationApi.unregisterClient(sourceId, client.getId());
		try {
			notificationApi.getClient(sourceId, client.getId());
		} catch (RetrofitError re) {
			return;
		}
		Assert.fail();
	}

}
