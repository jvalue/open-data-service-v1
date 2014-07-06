package org.jvalue.ods.notifications.definitions;

import org.jvalue.ods.notifications.NotificationDefinition;
import org.jvalue.ods.notifications.NotificationSender;
import org.jvalue.ods.notifications.RestAdapter;
import org.jvalue.ods.notifications.clients.RestClient;
import org.jvalue.ods.notifications.rest.RestAdapterFactory;
import org.jvalue.ods.notifications.sender.SenderFactory;


final class RestDefinition implements NotificationDefinition<RestClient> {
	
	@Override
	public String getRestName() {
		return "/rest";
	}


	@Override
	public RestAdapter<RestClient> getRestAdapter() {
		return RestAdapterFactory.getRestAdapter();
	}


	@Override
	public NotificationSender<RestClient> getNotificationSender() {
		return SenderFactory.getRestSender();
	}

}
