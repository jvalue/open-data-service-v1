package org.jvalue.ods.notifications.sender;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import org.jvalue.ods.api.notifications.AmqpClient;
import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.main.GcmApiKey;

public class SenderModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder()
				.implement(new TypeLiteral<Sender<GcmClient>>() { }, GcmSender.class)
				.implement(new TypeLiteral<Sender<HttpClient>>() { }, HttpSender.class)
				.implement(new TypeLiteral<Sender<AmqpClient>>() { }, AmqpSender.class)
				.build(SenderFactory.class));
		bind(SenderCache.class).in(Singleton.class);
	}


	@Provides
	public com.google.android.gcm.server.Sender provideGcmSender(@GcmApiKey String gcmApiKey) {
		return new com.google.android.gcm.server.Sender(gcmApiKey);
	}

}
