package org.jvalue.ods.notifications.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.clients.DummyClient;
import org.restlet.Request;


public final class RestAdapterTest extends BaseAdapterTest {

	@Test
	public void testGetParameters() {

		RestAdapter<DummyClient> adapter = new DummyRestAdapter();
		Set<String> params = adapter.getParameters();

		assertNotNull(params);
		assertTrue(params.contains(PARAM_SOURCE));

	}


	@Test
	public void testClientAdapter() {

		RestAdapter<DummyClient> adapter = new DummyRestAdapter();

		Request request = createMockRequest();
		addParameter(request, PARAM_SOURCE, "source");

		Client client = adapter.toClient(request);

		assertNotNull(client);
		assertEquals(client.getSource(), "source");

	}

}
