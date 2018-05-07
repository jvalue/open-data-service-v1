package org.jvalue.ods.pubsub;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.jvalue.commons.utils.Log;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Publisher {

    private Connection connection;
    private Channel channel;
    @NotNull private String exchange;
    @NotNull private String host;

    public Publisher() {
    }


    public boolean connect(String host, String exchange) {
        this.host = host;
        this.exchange = exchange;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);

        try {
            Log.info("Connect to publish/subscribe server: " + toString());
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT);
        } catch (IOException | TimeoutException e) {
            Log.error("Unable to connect to publish/subscribe server: " + toString());
            return false;
        }

        return true;
    }


    public boolean publish(String message) {
        try {
            channel.basicPublish(exchange, "", null, message.getBytes());
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
        return "{host: '"+host+"', exchange_name: '"+exchange+"'}";
    }
}
