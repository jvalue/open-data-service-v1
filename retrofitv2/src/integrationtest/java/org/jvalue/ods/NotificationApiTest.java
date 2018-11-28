package org.jvalue.ods;

import jsonapi.ResponseBody;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.notifications.*;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.ClientWrapper;

import java.io.IOException;
import java.util.List;

public class NotificationApiTest extends AbstractApiTest {

	private final HttpClient httpClient = new HttpClient("someHttpId", "someCallbackUrl", false);
	private final GcmClient gcmClient = new GcmClient("someGcmId", "someDeviceId");


	@Test
	public void testClient() throws IOException {
		doTestRegisterClient(httpClient);
		doTestRegisterClient(gcmClient);

		doTestGetClient(httpClient);
		doTestGetClient(gcmClient);

		doTestGetAllClients();

		doTestUnregisterClients();
	}


	private void doTestRegisterClient(Client client) throws IOException {
		ClientWrapper wrapper = ClientWrapper.from(client);
		JsonApiRequest request = JsonApiRequest.from(wrapper);

		ResponseBody response = notificationApi.registerClient(sourceId, request);
		Client result = response.dataToTargetObject(Client.class);

		Assert.assertEquals(client, result);
	}


	private void doTestGetClient(Client client) {
		ResponseBody response = notificationApi.getClient(sourceId, client.getId());
		Client result = response.dataToTargetObject(Client.class);

		Assert.assertEquals(client, result);
	}


	private void doTestGetAllClients() {
		ResponseBody response = notificationApi.getAllClients(sourceId);
		List<Client> result = response.dataToTargetObjectList(Client.class);

		Assert.assertEquals(2, result.size());
		Assert.assertTrue(result.contains(httpClient));
		Assert.assertTrue(result.contains(gcmClient));
	}


	private void doTestUnregisterClients() {
		notificationApi.unregisterClient(sourceId, httpClient.getId());
		notificationApi.unregisterClient(sourceId, gcmClient.getId());

		ResponseBody response = notificationApi.getAllClients(sourceId);
		List<Client> result = response.dataToTargetObjectList(Client.class);

		Assert.assertTrue(result.isEmpty());
	}
}
