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
	private static final String HOST = "dummy-host";
	private static final String EXCHANGE = "dummy-exchange";

	private  NdsSender sender;
	private NdsClient client = new NdsClient("someId", HOST, EXCHANGE);

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
			publisher.connect(HOST, EXCHANGE, "topic"); result = true;
			publisher.publish((String) any, "EnvironmentalMeasurement.Berlin"); result = true;
		}};

		sender.onNewDataStart();
		sender.onNewDataItem(JsonMapper.valueToTree(weather));
		sender.onNewDataComplete();

		new Verifications() {{
			Assert.assertEquals(SenderResult.Status.SUCCESS, sender.getSenderResult().getStatus());
		}};
	}
}
