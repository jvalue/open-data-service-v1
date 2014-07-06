package org.jvalue.ods.notifications.sender;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.notifications.NotificationSender;
import org.jvalue.ods.notifications.clients.RestClient;


final class RestSender implements NotificationSender<RestClient> {
	

	@Override
	public void notifySourceChanged(DataSource source, RestClient client) {
		System.out.println("SENDING REST MESSAGES TO " + client.getRestUrl() + " FOR SOURCE " + source.getId());
	}

}
