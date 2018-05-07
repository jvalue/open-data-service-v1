package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.notifications.AmqpClient;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.pubsub.Publisher;

@RunWith(JMockit.class)
public class AmqpSenderTest {

    private static final String SOURCE_ID = "someSourceId";

    private final DataSource source = new DataSource(SOURCE_ID, null, null, null);

    private static final String HOST = "dummy-host";

    private static final String EXCHANGE = "dummy-exchange";

    private  AmqpSender sender;

    private AmqpClient client = new AmqpClient("someId", HOST, EXCHANGE);

    @Mocked
    private Publisher publisher;

    @Before
    public void setupSender() {
        sender = new AmqpSender(source, client, publisher);
    }

    @Test
    public void testSuccess() {
        new Expectations() {{
            publisher.connect(HOST, EXCHANGE); result = true;
            publisher.publish("{\"sourceId\":\"someSourceId\"}"); result = true;
        }};

        ObjectNode sentData = new ObjectNode(JsonNodeFactory.instance);
        sentData.put("sourceId", SOURCE_ID);

        sender.onNewDataStart();
        sender.onNewDataItem(sentData);
        sender.onNewDataComplete();

        new Verifications() {{
            Assert.assertEquals(SenderResult.Status.SUCCESS, sender.getSenderResult().getStatus());
        }};
    }

    @Test
    public void testConnectionError() {
        new Expectations() {{
            publisher.connect((String) any, (String) any); result = false;
            publisher.publish((String) any); result = true;
        }};

        sender.onNewDataStart();
        sender.onNewDataItem(new ObjectNode(JsonNodeFactory.instance));
        sender.onNewDataComplete();

        new Verifications() {{
            Assert.assertEquals(SenderResult.Status.ERROR, sender.getSenderResult().getStatus());
        }};
    }

    @Test
    public void testSentError() {
        new Expectations() {{
            publisher.connect((String) any, (String) any); result = true;
            publisher.publish((String) any); result = false;
        }};

        sender.onNewDataStart();
        sender.onNewDataItem(new ObjectNode(JsonNodeFactory.instance));
        sender.onNewDataComplete();

        new Verifications() {{
            Assert.assertEquals(SenderResult.Status.ERROR, sender.getSenderResult().getStatus());
        }};
    }
}
