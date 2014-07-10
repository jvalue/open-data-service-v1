package org.jvalue.ods.notifications.definitions;

import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.notifications.rest.RestAdapter;
import org.jvalue.ods.notifications.rest.RestAdapterFactory;
import org.jvalue.ods.notifications.sender.NotificationSender;
import org.jvalue.ods.notifications.sender.SenderFactory;


final class GcmDefinition implements NotificationDefinition<GcmClient> {
	
	@Override
	public String getRestName() {
		return "/gcm";
	}


	@Override
	public RestAdapter<GcmClient> getRestAdapter() {
		return RestAdapterFactory.getGcmAdapter();
	}


	@Override
	public NotificationSender<GcmClient> getNotificationSender() {
		return SenderFactory.getGcmSender();
	}

}
