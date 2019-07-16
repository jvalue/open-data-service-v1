/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.api.notifications;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public final class ClientVisitorTest {

	@Test
	public final void testSimpleEcho() {

		DummyClientVisitor visitor = new DummyClientVisitor();

		GcmClient gcmClient = new GcmClient("", "");
		HttpClient httpClient = new HttpClient("", "", true);

		assertEquals(gcmClient.accept(visitor, "param"), "param");
		assertEquals(httpClient.accept(visitor, "param"), "param");

	}


	private static final class DummyClientVisitor implements ClientVisitor<String, String> {

		@Override
		public String visit(GcmClient client, String param) {
			return param;
		}


		@Override
		public String visit(HttpClient client, String param) {
			return param;
		}

		@Override
		public String visit(AmqpClient client, String param) { return param; }

		@Override
		public String visit(NdsClient client, String param) { return param; }

	}

}
