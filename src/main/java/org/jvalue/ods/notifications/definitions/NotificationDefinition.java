package org.jvalue.ods.notifications.definitions;

import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.rest.RestAdapter;
import org.jvalue.ods.notifications.sender.NotificationSender;



public interface NotificationDefinition<C extends Client> {
	
	public String getRestName();
	public RestAdapter<C> getRestAdapter();
	public NotificationSender<C> getNotificationSender();

}
