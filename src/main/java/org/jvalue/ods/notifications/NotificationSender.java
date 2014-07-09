package org.jvalue.ods.notifications;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericEntity;


public interface NotificationSender<T extends Client> {
	
	public void notifySourceChanged(
			T client, 
			DataSource source, 
			GenericEntity data) throws NotificationException;

}
