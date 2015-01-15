package org.jvalue.ods.notifications;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import org.jvalue.ods.notifications.sender.SenderModule;

public class NotificationsModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new SenderModule());
		bind(NotificationManager.class).in(Singleton.class);
	}

}
