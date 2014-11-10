package org.jvalue.ods.notifications;


import com.google.inject.AbstractModule;

public class NotificationsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ClientRepository.class);
	}

}
