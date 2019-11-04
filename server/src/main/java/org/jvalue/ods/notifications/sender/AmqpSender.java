/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.notifications.AmqpClient;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.pubsub.Publisher;

public class AmqpSender extends AbstractSender<AmqpClient> {

    private final ArrayNode buffer = new ArrayNode(JsonNodeFactory.instance);
    private final Publisher publisher;

    @Inject
    protected AmqpSender(@Assisted DataSource source, @Assisted AmqpClient client, Publisher publisher) {
        super(source, client);
        this.publisher = publisher;
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
        boolean connected = publisher.connect(client.getUri(),client.getExchange(), client.getExchangeType());

        boolean sent = true;

        for (JsonNode node : buffer) {
            boolean nodeSent = publisher.publish(node.toString(), client.getRoutingKey());
            if (!nodeSent) sent = false;
        }

        publisher.close();

        if (!connected) {
            setErrorResult("Unable to connect to publish/subscribe server.");
        } else if (!sent) {
            setErrorResult("Unable to send message to publish/subscribe server.");
        } else {
            setSuccessResult();
        }
    }
}
