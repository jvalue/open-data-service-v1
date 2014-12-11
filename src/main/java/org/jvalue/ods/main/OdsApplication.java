package org.jvalue.ods.main;


import com.google.inject.Guice;
import com.google.inject.Injector;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.jvalue.ods.configuration.ConfigurationModule;
import org.jvalue.ods.data.DataModule;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.db.DbModule;
import org.jvalue.ods.filter.FilterModule;
import org.jvalue.ods.monitoring.DbHealthCheck;
import org.jvalue.ods.notifications.NotificationsModule;
import org.jvalue.ods.rest.DataApi;
import org.jvalue.ods.rest.DataSourceApi;
import org.jvalue.ods.rest.DataViewApi;
import org.jvalue.ods.rest.DbExceptionMapper;
import org.jvalue.ods.rest.FilterChainApi;
import org.jvalue.ods.rest.FilterDescriptionApi;
import org.jvalue.ods.rest.JsonMixins;
import org.jvalue.ods.rest.NotificationClientRegistrationApi;
import org.jvalue.ods.rest.PluginApi;
import org.jvalue.ods.rest.RestModule;

import java.util.Map;

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
		JsonMixins mixins = new JsonMixins();
		for (Map.Entry<Class<?>, Class<?>> mixinPair : mixins.getMixins().entrySet()) {
			configuration.getObjectMapper().addMixInAnnotations(mixinPair.getKey(), mixinPair.getValue());
		}
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

		// start data grabbing
		environment.lifecycle().manage(injector.getInstance(DataSourceManager.class));
		environment.jersey().getResourceConfig().register(MultiPartFeature.class);
		environment.jersey().register(injector.getInstance(DataSourceApi.class));
		environment.jersey().register(injector.getInstance(DataApi.class));
		environment.jersey().register(injector.getInstance(FilterChainApi.class));
		environment.jersey().register(injector.getInstance(DataViewApi.class));
		environment.jersey().register(injector.getInstance(NotificationClientRegistrationApi.class));
		environment.jersey().register(injector.getInstance(PluginApi.class));
		environment.jersey().register(injector.getInstance(FilterDescriptionApi.class));
		environment.jersey().register(new DbExceptionMapper());
		environment.healthChecks().register(DbHealthCheck.class.getSimpleName(), injector.getInstance(DbHealthCheck.class));
	}

}
