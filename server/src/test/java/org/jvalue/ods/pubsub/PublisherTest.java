package org.jvalue.ods.pubsub;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.charset.StandardCharsets;

@RunWith(JMockit.class)
public class PublisherTest {

	private static final String HOST = "dummy-host";
	private static final String EXCHANGE = "dummy-exchange";
	private static final String EXCHANGE_TYPE = "topic";
	private static final String MESSAGE = "{name: 'test'}";
	private static final String ROUTING_KEY = "main.test";

	@Mocked
	ConnectionFactory factory;
	@Mocked
	Channel channel;
	@Mocked
	Connection connection;

	private Publisher publisher;

	@Before
	public void setUp() {
		publisher = new Publisher(factory);
	}


	@Test
	public void testPublish() throws Exception {
		new Expectations() {{
			channel.exchangeDeclare(EXCHANGE, EXCHANGE_TYPE);
			channel.basicPublish(EXCHANGE, ROUTING_KEY, null, MESSAGE.getBytes(StandardCharsets.UTF_8));
			channel.isOpen(); result = true;
			channel.close();

			connection.createChannel(); result = channel;
			connection.isOpen(); result = true;
			connection.close();

			factory.setHost(HOST);
			factory.newConnection(); result = connection;
		}};

		publisher.connect(HOST, EXCHANGE, EXCHANGE_TYPE);
		publisher.publish(MESSAGE, ROUTING_KEY);
		publisher.close();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testExchangeType_DIRECT () {
		publisher.connect(HOST, EXCHANGE, BuiltinExchangeType.DIRECT.getType());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testExchangeType_HEADERS () {
		publisher.connect(HOST, EXCHANGE, BuiltinExchangeType.HEADERS.getType());
	}
}
