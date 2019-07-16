/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.notifications.sender;


import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.sources.DataSource;

@RunWith(JMockit.class)
public final class SenderCacheTest {

	@Mocked private SenderFactory senderFactory;

	@Test
	public void testGetAndRelease() {
		SenderCache cache = new SenderCache(senderFactory);

		final HttpClient httpClient = new HttpClient("someId", "someUrl", false);
		final GcmClient gcmClient = new GcmClient("someId", "someDeviceId");
		final DataSource source = new DataSource("someId", null, null, null);

		cache.get(source, httpClient);
		cache.get(source, httpClient);
		cache.get(source, gcmClient);
		cache.get(source, gcmClient);

		new Verifications() {{
			senderFactory.createHttpSender(source, httpClient); times = 1;
			senderFactory.createGcmSender(source, gcmClient); times = 1;
		}};
	}

}
