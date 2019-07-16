/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.api.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;


public final class HttpClientTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	@Test
	public final void testEquals() {

		HttpClient client1 = new HttpClient("0", "url1", true);
		HttpClient client2 = new HttpClient("0", "url1", true);
		HttpClient client3 = new HttpClient("0", "url2", true);
		HttpClient client4 = new HttpClient("0", "url1", false);
		HttpClient client5 = new HttpClient("1", "url1", true);

		assertEquals(client1, client1);
		assertEquals(client1, client2);
		assertNotEquals(client1, client3);
		assertNotEquals(client1, client4);
		assertNotEquals(client1, client5);
		assertNotEquals(client1, null);
		assertNotEquals(client1, new Object());

		assertEquals(client1.hashCode(), client1.hashCode());
		assertEquals(client1.hashCode(), client2.hashCode());
		assertNotEquals(client1.hashCode(), client3.hashCode());
		assertNotEquals(client1.hashCode(), client4.hashCode());
		assertNotEquals(client1.hashCode(), client5.hashCode());
	}


	@Test
	public final void testGet() {
		HttpClient client = new HttpClient("0", "url", true);
		assertEquals(client.getId(), "0");
		assertEquals(client.getCallbackUrl(), "url");
		assertEquals(client.getSendData(), true);

	}


	@Test
	public final void testJson() throws JsonProcessingException {
		HttpClient client = new HttpClient("id", "url", false);
		JsonNode json = mapper.valueToTree(client);
		assertNotNull(json);
		assertEquals(client, mapper.treeToValue(json, HttpClient.class));

	}

}
