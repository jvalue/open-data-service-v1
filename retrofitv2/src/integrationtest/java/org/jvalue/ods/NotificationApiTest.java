package org.jvalue.ods;

import jsonapi.ResponseBody;
import org.junit.Test;
import org.jvalue.ods.api.notifications.*;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.ClientWrapper;

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


	@Test
	public void testUnregisterClient() {

	}

	private void doTestClient(Client client, ClientDescription clientDescription) throws IOException {
		ClientWrapper wrapper = ClientWrapper.from(client);
		JsonApiRequest request = JsonApiRequest.from(wrapper);

		ResponseBody response = notificationApi.registerClient(sourceId, request);
	}


}
