package org.jvalue.ods.main;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

import org.jvalue.ods.data.DataSourceConfiguration;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.configuration.ConfigurationModule;
import org.jvalue.ods.db.DbModule;
import org.jvalue.ods.filter.FilterModule;
import org.jvalue.ods.notifications.NotificationsModule;
import org.jvalue.ods.rest.NotificationClientRegistrationApi;

import javax.ws.rs.core.Context;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public final class OdsApplication extends Application<OdsConfig> {


	public static void main(String[] args) throws Exception {
		new OdsApplication().run(args);
	}


	@Override
	public String getName() {
		return "Open Data Service";
	}


	@Override
	public void initialize(Bootstrap<OdsConfig>configuration) {
		// nothing to do here
	}


	@Override
	@Context
	public void run(OdsConfig configuration, Environment environment) {
		Injector injector = Guice.createInjector(
				new ConfigModule(configuration),
				new ConfigurationModule(),
				new DbModule(),
				new NotificationsModule(),
				new FilterModule());

		// static configuration for now
		DataSourceManager configManager = injector.getInstance(DataSourceManager.class);
		configManager.addConfiguration(
				injector.getInstance(
						Key.get(
								DataSourceConfiguration.class,
								Names.named(ConfigurationModule.NAME_PEGELONLINE_CONFIGURATION))));

		// start data grabbing
		environment.lifecycle().manage(injector.getInstance(DataGrabberManager.class));
		environment.jersey().register(injector.getInstance(NotificationClientRegistrationApi.class));
	}

}
