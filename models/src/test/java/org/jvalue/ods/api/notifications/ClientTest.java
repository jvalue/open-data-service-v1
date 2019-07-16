/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.api.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.Assert;
import org.junit.Test;


public final class ClientTest {

	@Test
	public final void testEquals() {

		DummyClient client1 = new DummyClient("0", "source1");
		DummyClient client2 = new DummyClient("0", "source1");
		DummyClient client3 = new DummyClient("1", "source1");
		DummyClient client4 = new DummyClient("0", "source2");

		Assert.assertEquals(client1, client1);
		Assert.assertEquals(client1, client2);
		Assert.assertNotEquals(client1, client3);
		Assert.assertNotEquals(client1, client4);
		Assert.assertNotEquals(client1, null);
		Assert.assertNotEquals(client1, new Object());

		Assert.assertEquals(client1.hashCode(), client1.hashCode());
		Assert.assertEquals(client1.hashCode(), client2.hashCode());
		Assert.assertNotEquals(client1.hashCode(), client3.hashCode());
		Assert.assertNotEquals(client1.hashCode(), client4.hashCode());
	}


	@Test
	public final void testGet() {

		DummyClient client = new DummyClient("0", "source");
		Assert.assertEquals(client.getId(), "0");
		Assert.assertEquals(client.getType(), "source");

	}


	private static final class DummyClient extends Client {

		@JsonCreator
		public DummyClient(
				@JsonProperty("clientId") String clientId,
				@JsonProperty("source") String source) {

			super(clientId, source);
		}


		public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
			throw new UnsupportedOperationException("dummy");
		}

	}

}
