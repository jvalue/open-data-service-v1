package org.jvalue.ods.notifications.sender;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.notifications.clients.Client;


public interface NotificationSender<T extends Client> {
	
	public void notifySourceChanged(
			T client, 
			DataSource source, 
			GenericEntity data) throws NotificationException;

}
