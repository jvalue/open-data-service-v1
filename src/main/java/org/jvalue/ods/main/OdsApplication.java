package org.jvalue.ods.main;


import com.google.inject.Guice;
import com.google.inject.Injector;

import org.jvalue.ods.db.DbModule;
import org.jvalue.ods.grabber.DataGrabberMain;
import org.jvalue.ods.notifications.NotificationsModule;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public final class OdsApplication extends Application<OdsConfiguration> {


	public static void main(String[] args) throws Exception {
		new OdsApplication().run(args);
	}


	@Override
	public String getName() {
		return "Open Data Service";
	}


	@Override
	public void initialize(Bootstrap<OdsConfiguration>configuration) {
		// nothing to do here
	}


	@Override
	public void run(OdsConfiguration configuration, Environment environment) {
		Injector injector = Guice.createInjector(
				new DbModule(),
				new NotificationsModule());


		// start data grabbing
		environment.lifecycle().manage(new DataGrabberMain());
	}

}
