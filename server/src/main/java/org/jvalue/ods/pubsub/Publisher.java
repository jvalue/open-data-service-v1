package org.jvalue.ods.pubsub;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.jvalue.commons.utils.Log;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Publisher {

	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	@NotNull private String exchange;
	@NotNull private String host;

	@Inject
	public Publisher(ConnectionFactory factory) {
		this.factory = factory;
	}


	public boolean connect(String host, String exchange, String exchangeType) {
		assertIsSupportedExchangeType(exchangeType);

		this.host = host;
		this.exchange = exchange;

		factory.setHost(host);

		try {
			Log.info("Connect to publish/subscribe server: " + toString());
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(exchange, exchangeType);
		} catch (IOException | TimeoutException e) {
			Log.error("Unable to connect to publish/subscribe server: " + toString());
			return false;
		}

		return true;
	}


	public boolean publish(String message) {
		return publish(message, "");
	}


	public boolean publish(String message, String routingKey) {
		try {
			channel.basicPublish(exchange, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
			Log.debug("[x] Sent '" + message + "' to " + toString());
			return true;
		} catch (NullPointerException | IOException e) {
			Log.debug("[ ] Sent failed '" + message + "' to " + toString());
			return false;
		}
	}


	public void close() {
		Log.info("Close connection to publish/subscribe server: " + toString());
		try {
			if (channel != null && channel.isOpen()) channel.close();
			if (connection != null && connection.isOpen()) connection.close();
		} catch (IOException | TimeoutException e) {
		}
	}


	public String toString() {
		return "{host: '" + host + "', exchange_name: '" + exchange + "'}";
	}


	private void assertIsSupportedExchangeType(String type) {
		if (!BuiltinExchangeType.FANOUT.getType().equals(type)
			&& !BuiltinExchangeType.TOPIC.getType().equals(type)) {

			throw new IllegalArgumentException(
				"Exchange type '" + type + "' is not supported. Supported types are [fanout, topic].");
		}
	}

}
