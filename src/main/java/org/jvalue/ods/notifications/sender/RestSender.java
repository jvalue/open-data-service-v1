package org.jvalue.ods.notifications.sender;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.notifications.NotificationException;
import org.jvalue.ods.notifications.NotificationSender;
import org.jvalue.ods.notifications.clients.RestClient;
import org.jvalue.ods.utils.RestCall;
import org.jvalue.ods.utils.RestException;


final class RestSender implements NotificationSender<RestClient> {
	

	@Override
	public void notifySourceChanged(DataSource source, RestClient client) throws NotificationException {
		try {
			new RestCall.Builder(RestCall.RequestType.POST, client.getRestUrl())
				.parameter(client.getSourceParam(), source.getId())
				.build()
				.execute();
		} catch (RestException re) {
			throw new NotificationException(re);
		}
	}

}
