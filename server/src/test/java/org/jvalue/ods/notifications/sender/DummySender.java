package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.notifications.DummyClient;


final class DummySender extends Sender<DummyClient> {

	protected DummySender(DataSource source, DummyClient client) {
		super(source, client);
	}

	@Override
	public void onNewDataStart() {
		throw new UnsupportedOperationException("dummy");
	}

	@Override
	public void onNewDataItem(ObjectNode data) {
		throw new UnsupportedOperationException("dummy");
	}

	@Override
	public void onNewDataComplete() {
		throw new UnsupportedOperationException("dummy");
	}

}
