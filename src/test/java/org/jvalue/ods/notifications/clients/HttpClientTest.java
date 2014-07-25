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


public final class HttpClientTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	@Test
	public final void testEquals() {

		HttpClient client1 = new HttpClient("0", "source1", "url1", "param1", true);
		HttpClient client2 = new HttpClient("0", "source1", "url1", "param1", true);
		HttpClient client3 = new HttpClient("0", "source1", "url2", "param1", true);
		HttpClient client4 = new HttpClient("0", "source1", "url1", "param1", false);
		HttpClient client5 = new HttpClient("0", "source2", "url1", "param1", true);
		HttpClient client6 = new HttpClient("1", "source1", "url1", "param1", true);

		assertEquals(client1, client1);
		assertEquals(client1, client2);
		assertNotEquals(client1, client3);
		assertNotEquals(client1, client4);
		assertNotEquals(client1, client5);
		assertNotEquals(client1, client6);
		assertNotEquals(client1, null);
		assertNotEquals(client1, new Object());

		assertEquals(client1.hashCode(), client1.hashCode());
		assertEquals(client1.hashCode(), client2.hashCode());
		assertNotEquals(client1.hashCode(), client3.hashCode());
		assertNotEquals(client1.hashCode(), client4.hashCode());
		assertNotEquals(client1.hashCode(), client5.hashCode());

		assertTrue(client1.equalsIgnoreId(client2));
		assertFalse(client1.equalsIgnoreId(client3));
		assertFalse(client1.equalsIgnoreId(client4));
		assertFalse(client1.equalsIgnoreId(client5));
		assertTrue(client1.equalsIgnoreId(client6));
		
	}


	@Test
	public final void testGet() {

		HttpClient client = new HttpClient("0", "source", "url", "param", true);
		assertEquals(client.getClientId(), "0");
		assertEquals(client.getSource(), "source");
		assertEquals(client.getRestUrl(), "url");
		assertEquals(client.getSourceParam(), "param");
		assertEquals(client.getSendData(), true);

	}


	@Test
	public final void testJson() throws JsonProcessingException {

		HttpClient client = new HttpClient("id", "source", "url", "param", false);
		JsonNode json = mapper.valueToTree(client);
		assertNotNull(json);
		assertEquals(client, mapper.treeToValue(json, HttpClient.class));

	}

}
