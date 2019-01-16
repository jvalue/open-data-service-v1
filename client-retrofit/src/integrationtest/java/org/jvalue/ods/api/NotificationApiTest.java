/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.api;


import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.notifications.*;
import retrofit.RetrofitError;

import java.util.List;

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

		notificationApi.registerClientSynchronously(sourceId, httpClient.getId(), httpClientDescription);
		notificationApi.registerClientSynchronously(sourceId, gcmClient.getId(), gcmClientDescription);
		List<Client> clients = notificationApi.getAllClientsSynchronously(sourceId);

		Assert.assertEquals(2, clients.size());
		Assert.assertTrue(clients.contains(httpClient));
		Assert.assertTrue(clients.contains(gcmClient));
	}


	private void testClient(Client client, ClientDescription clientDescription) {
		notificationApi.registerClientSynchronously(sourceId, client.getId(), clientDescription);
		Assert.assertEquals(client, notificationApi.getClientSynchronously(sourceId, client.getId()));
		notificationApi.unregisterClientSynchronously(sourceId, client.getId());
		try {
			notificationApi.getClientSynchronously(sourceId, client.getId());
		} catch (RetrofitError re) {
			return;
		}
		Assert.fail();
	}

}
