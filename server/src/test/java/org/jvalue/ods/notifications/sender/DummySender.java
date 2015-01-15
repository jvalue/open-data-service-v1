package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.notifications.DummyClient;


final class DummySender extends Sender<DummyClient> {

	protected DummySender(DummyClient client) {
		super(client);
	}

	@Override
	public void onNewDataStart(DataSource source) {
		throw new UnsupportedOperationException("dummy");
	}

	@Override
	public void onNewDataItem(DataSource source, ObjectNode data) {
		throw new UnsupportedOperationException("dummy");
	}

	@Override
	public void onNewDataComplete(DataSource source) {
		throw new UnsupportedOperationException("dummy");
	}

}
