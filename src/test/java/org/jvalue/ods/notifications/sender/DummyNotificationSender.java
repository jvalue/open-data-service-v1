package org.jvalue.ods.notifications.sender;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.notifications.clients.DummyClient;


final class DummyNotificationSender extends NotificationSender<DummyClient> {

	@Override
	public SenderResult notifySourceChanged(
			DummyClient client,
			DataSource source,
			Object object) {
		throw new UnsupportedOperationException("dummy");
	}

}
