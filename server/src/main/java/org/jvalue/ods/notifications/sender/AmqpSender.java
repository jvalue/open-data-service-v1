package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.notifications.AmqpClient;
import org.jvalue.ods.api.sources.DataSource;

public class AmqpSender extends AbstractSender<AmqpClient> {

    private final ArrayNode buffer = new ArrayNode(JsonNodeFactory.instance);

    @Inject
    protected AmqpSender(@Assisted DataSource source, @Assisted AmqpClient client) {
        super(source, client);
    }


    @Override
    public void onNewDataStart() {
        // nothing to do here
    }


    @Override
    public void onNewDataItem(ObjectNode data) {
        buffer.add(data);
    }


    @Override
    public void onNewDataComplete() {
        System.out.println("AMQP: onNewDataComplete");
        buffer.forEach(node -> System.out.println(node));
        setSuccessResult();
    }
}
