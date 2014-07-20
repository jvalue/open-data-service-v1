package org.jvalue.ods.notifications.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.Test;
import org.jvalue.ods.notifications.clients.GcmClient;
import org.restlet.Request;


public final class GcmAdapterTest extends BaseAdapterTest {

	private final String
		PARAM_GCM_CLIENT_ID = "gcmClientId";

	@Test
	public void testGetParameters() {

		GcmAdapter adapter = new GcmAdapter();
		Set<String> params = adapter.getParameters();
		assertEquals(params.size(), 2);

	}


	@Test
	public void testClientAdapter() {

		GcmAdapter adapter = new GcmAdapter();
		Request request = createMockRequest();
		addParameter(request, PARAM_SOURCE, "source");
		addParameter(request, PARAM_GCM_CLIENT_ID, "someId");

		GcmClient client = adapter.toClient(request);

		assertNotNull(client);

		assertNotNull(client.getClientId());
		assertEquals("source", client.getSource());
		assertEquals("someId", client.getGcmClientId());

	}

}
