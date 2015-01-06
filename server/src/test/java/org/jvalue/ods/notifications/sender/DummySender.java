package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.node.ArrayNode;

import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.notifications.DummyClient;


final class DummySender extends Sender<DummyClient> {

	@Override
	public SenderResult notifySourceChanged(
			DummyClient client,
			DataSource source,
			ArrayNode object) {
		throw new UnsupportedOperationException("dummy");
	}

}
