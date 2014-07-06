package org.jvalue.ods.notifications;

import org.jvalue.ods.data.DataSource;


public interface NotificationSender<T extends Client> {
	
	public void notifySourceChanged(DataSource source, T client) throws NotificationException;

}
