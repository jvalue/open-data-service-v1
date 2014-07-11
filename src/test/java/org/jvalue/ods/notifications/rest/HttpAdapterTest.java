package org.jvalue.ods.notifications.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.jvalue.ods.notifications.clients.HttpClient;
import org.restlet.Request;


public final class HttpAdapterTest extends BaseAdapterTest {

	private final String
		PARAM_URL = "restUrl",
		PARAM_PARAM = "restParam",
		PARAM_SEND_DATA = "sendData";

	@Test
	public void testGetParameters() {

		HttpAdapter adapter = new HttpAdapter();
		Set<String> params = adapter.getParameters();
		assertEquals(params.size(), 5);
		assertTrue(params.contains(PARAM_URL));
		assertTrue(params.contains(PARAM_PARAM));
		assertTrue(params.contains(PARAM_SEND_DATA));

	}


	@Test
	public void testClientAdapter() {

		HttpAdapter adapter = new HttpAdapter();
		Request request = createMockRequest();
		addParameter(request, PARAM_ID, "id");
		addParameter(request, PARAM_SOURCE, "source");
		addParameter(request, PARAM_URL, "url");
		addParameter(request, PARAM_PARAM, "param");
		addParameter(request, PARAM_SEND_DATA, Boolean.TRUE.toString());

		HttpClient client = adapter.toClient(request);

		assertNotNull(client);
		assertEquals(client, new HttpClient("id", "source", "url", "param", true));

	}

}
