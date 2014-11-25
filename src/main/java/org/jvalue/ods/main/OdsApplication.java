package org.jvalue.ods.main;


import com.google.inject.Guice;
import com.google.inject.Injector;

import org.jvalue.ods.configuration.ConfigurationModule;
import org.jvalue.ods.configuration.PegelOnlineConfigurationFactory;
import org.jvalue.ods.configuration.PegelPortalMvConfigurationFactory;
import org.jvalue.ods.data.DataModule;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.db.DbModule;
import org.jvalue.ods.filter.FilterModule;
import org.jvalue.ods.notifications.NotificationsModule;
import org.jvalue.ods.rest.DefaultDataSourceApi;
import org.jvalue.ods.rest.NotificationClientRegistrationApi;
import org.jvalue.ods.rest.RestModule;

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
				new RestModule(),
				new ConfigurationModule(),
				new DbModule(),
				new NotificationsModule(),
				new FilterModule(),
				new DataModule());

		// static configuration for now
		DataSourceManager configManager = injector.getInstance(DataSourceManager.class);
		configManager.addConfiguration(injector.getInstance(PegelOnlineConfigurationFactory.class).createConfiguration());
		configManager.addConfiguration(injector.getInstance(PegelPortalMvConfigurationFactory.class).createConfiguration());

		// start data grabbing
		environment.lifecycle().manage(injector.getInstance(DataGrabberManager.class));
		environment.jersey().register(injector.getInstance(NotificationClientRegistrationApi.class));
		environment.jersey().register(injector.getInstance(DefaultDataSourceApi.class));
	}

}
