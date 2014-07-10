package org.jvalue.ods.notifications.definitions;

import org.jvalue.ods.notifications.clients.HttpClient;
import org.jvalue.ods.notifications.rest.RestAdapter;
import org.jvalue.ods.notifications.rest.RestAdapterFactory;
import org.jvalue.ods.notifications.sender.NotificationSender;
import org.jvalue.ods.notifications.sender.SenderFactory;


final class HttpDefinition implements NotificationDefinition<HttpClient> {
	
	@Override
	public String getRestName() {
		return "/rest";
	}


	@Override
	public RestAdapter<HttpClient> getRestAdapter() {
		return RestAdapterFactory.getRestAdapter();
	}


	@Override
	public NotificationSender<HttpClient> getNotificationSender() {
		return SenderFactory.getRestSender();
	}

}
