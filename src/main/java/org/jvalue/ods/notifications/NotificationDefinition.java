package org.jvalue.ods.notifications;



public interface NotificationDefinition<C extends Client> {
	
	public String getRestName();
	public RestAdapter<C> getRestAdapter();
	public NotificationSender<C> getNotificationSender();

}
