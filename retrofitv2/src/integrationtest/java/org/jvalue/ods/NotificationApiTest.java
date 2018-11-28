package org.jvalue.ods;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jsonapi.ResponseBody;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.notifications.*;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.ClientWrapper;
import org.jvalue.ods.utils.JsonMapper;

import java.io.IOException;

public class NotificationApiTest extends AbstractApiTest {

	private final HttpClient httpClient = new HttpClient("someHttpId", "someCallbackUrl", false);
	private final GcmClient gcmClient = new GcmClient("someGcmId", "someDeviceId");

	private final ClientDescription
		httpClientDescription = new HttpClientDescription(httpClient.getCallbackUrl(), httpClient.getSendData()),
		gcmClientDescription = new GcmClientDescription(gcmClient.getGcmClientId());

	@Test
	public void testClient() {
	}


	@Test
	public void testRegisterClient() throws IOException {
		doTestClient(httpClient, httpClientDescription);
	}


	public void doTestGetAllClients() {
	}


	private void doTestClient(Client client, ClientDescription clientDescription) throws IOException {
		ClientWrapper wrapper = ClientWrapper.from(client);
		JsonApiRequest request = JsonApiRequest.from(wrapper);

		notificationApi.registerClient(sourceId, request);
		ResponseBody response = notificationApi.getClient(sourceId, client.getId());
		Client result = requestToClient(response);

		Assert.assertEquals(client, result);
	}


	private Client requestToClient(ResponseBody response) {
		((ObjectNode) response.getData().get("attributes")).put("id", response.getId());
		((ObjectNode) response.getData().get("attributes")).put("type", response.getType());
		return JsonMapper.convertValue(
			response.getData().get("attributes"),
			Client.class);
	}


}
