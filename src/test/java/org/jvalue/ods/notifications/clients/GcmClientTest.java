package org.jvalue.ods.notifications.clients; 

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public final class GcmClientTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	@Test
	public final void testEquals() {

		GcmClient client1 = new GcmClient("0", "source1", "0");
		GcmClient client2 = new GcmClient("0", "source1", "0");
		GcmClient client3 = new GcmClient("1", "source1", "0");
		GcmClient client4 = new GcmClient("0", "source2", "1");

		assertEquals(client1, client1);
		assertEquals(client1, client2);
		assertNotEquals(client1, client3);
		assertNotEquals(client1, client4);
		assertNotEquals(client1, null);
		assertNotEquals(client1, new Object());

		assertEquals(client1.hashCode(), client1.hashCode());
		assertEquals(client1.hashCode(), client2.hashCode());
		assertNotEquals(client1.hashCode(), client3.hashCode());
		assertNotEquals(client1.hashCode(), client4.hashCode());

		assertTrue(client1.equalsIgnoreId(client2));
		assertTrue(client1.equalsIgnoreId(client3));
		assertFalse(client1.equalsIgnoreId(client4));
		
	}


	@Test
	public final void testGet() {

		GcmClient client = new GcmClient("0", "source", "1");
		assertEquals(client.getClientId(), "0");
		assertEquals(client.getSource(), "source");
		assertEquals(client.getGcmClientId(), "1");

	}

	

	@Test
	public final void testJson() throws JsonProcessingException {

		GcmClient client = new GcmClient("id", "source", "gcm");
		JsonNode json = mapper.valueToTree(client);
		assertNotNull(json);
		assertEquals(client, mapper.treeToValue(json, GcmClient.class));

	}

}
