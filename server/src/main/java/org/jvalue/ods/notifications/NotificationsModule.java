package org.jvalue.ods.notifications;


import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.notifications.clients.HttpClient;
import org.jvalue.ods.notifications.sender.GcmSender;
import org.jvalue.ods.notifications.sender.HttpSender;
import org.jvalue.ods.notifications.sender.Sender;
import org.jvalue.ods.notifications.sender.SenderVisitor;

public class NotificationsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<Sender<GcmClient>>(){ }).to(GcmSender.class);
		bind(new TypeLiteral<Sender<HttpClient>>(){ }).to(HttpSender.class);
		bind(SenderVisitor.class);
	}

}
