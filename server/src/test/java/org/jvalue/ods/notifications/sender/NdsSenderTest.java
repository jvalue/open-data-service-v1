/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.notifications.sender;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.notifications.NdsClient;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.adapter.domain.weather.models.Location;
import org.jvalue.ods.processor.adapter.domain.weather.models.Temperature;
import org.jvalue.ods.processor.adapter.domain.weather.models.TemperatureType;
import org.jvalue.ods.processor.adapter.domain.weather.models.Weather;
import org.jvalue.ods.pubsub.Publisher;
import org.jvalue.ods.utils.JsonMapper;

import java.time.Instant;

@RunWith(JMockit.class)
public class NdsSenderTest {

	private static final String SOURCE_ID = "someSourceId";
	private final DataSource source = new DataSource(SOURCE_ID, null, null, null);
	private static final String URI = "amqp://userName:password@hostName:portNumber/virtualHost";
	private static final String EXCHANGE = "dummy-exchange";

	private  NdsSender sender;
	private NdsClient client = new NdsClient("someId", URI, EXCHANGE, false);

	private final Weather weather = new Weather(
		"42",
		new Temperature(30, TemperatureType.CELSIUS),
		null,
		50,
		Instant.now(),
		new Location("Berlin", null, null));

	@Mocked
	private Publisher publisher;

	@Before
	public void setupSender() {
		sender = new NdsSender(source, client, publisher);
	}

	@Test
	public void testMessageTransformation(){
		new Expectations() {{
			publisher.connect(URI, EXCHANGE, "topic"); result = true;
			publisher.publish((String) any, "1.0.0.EnvironmentalMeasurement.Berlin"); result = true;
		}};

		sender.onNewDataStart();
		sender.onNewDataItem(JsonMapper.valueToTree(weather));
		sender.onNewDataComplete();

		new Verifications() {{
			Assert.assertEquals(SenderResult.Status.SUCCESS, sender.getSenderResult().getStatus());

			String cimMsg;
			publisher.publish(cimMsg = withCapture(), (String) any);
			Assert.assertTrue(cimMsg.contains("<cim:name>Berlin</cim:name>"));
			Assert.assertTrue(cimMsg.contains("<cim:value>30.0</cim:value>"));
		}};
	}

}
