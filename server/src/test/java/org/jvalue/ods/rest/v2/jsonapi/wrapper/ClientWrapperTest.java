package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.utils.JsonMapper;

public class ClientWrapperTest {

	private final Client HTTPClient = new HttpClient("id", "callback", false);

	private final ClientWrapper httpWrapped = ClientWrapper.from(HTTPClient);


	@Test
	public void testSerialization() throws JsonProcessingException {

		String expectedJson = "{\"callbackUrl\":\"callback\",\"sendData\":false,\"id\":\"id\",\"type\":\"HTTP\"}";
		String result = JsonMapper.writeValueAsString(httpWrapped);

		Assert.assertEquals(expectedJson, result);
	}
}
