package org.jvalue.ods.api.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class AmqpClientTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public final void testEquals() {

        AmqpClient client1 = new AmqpClient("0", "host1", "exchange", "topic", "x.y.z");
        AmqpClient client2 = new AmqpClient("0", "host1", "exchange", "topic", "x.y.z");
        AmqpClient client3 = new AmqpClient("0", "host2", "exchange", "topic", "x.y.z");
        AmqpClient client4 = new AmqpClient("0", "host1", "exchange-other", "topic", "x.y.z");
        AmqpClient client5 = new AmqpClient("1", "host1", "exchange", "topic", "x.y.z");

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
        AmqpClient client = new AmqpClient("0", "host1", "exchange", "topic", "abcRouting");
        assertEquals(client.getId(), "0");
        assertEquals(client.getHost(), "host1");
        assertEquals(client.getExchange(), "exchange");
        assertEquals(client.getExchangeType(), "topic");
        assertEquals(client.getRoutingKey(), "abcRouting");
    }


    @Test
    public final void testJson() throws JsonProcessingException {
        AmqpClient client =new AmqpClient("0", "host1", "exchange", "topic", "main.value");
        JsonNode json = mapper.valueToTree(client);
        assertNotNull(json);
        assertEquals(client, mapper.treeToValue(json, AmqpClient.class));

    }

}
