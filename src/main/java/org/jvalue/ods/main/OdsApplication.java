package org.jvalue.ods.main;


import org.jvalue.ods.grabber.DataGrabberMain;

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
		// Injector injector = Guice.createInjector();

		// start data grabbing
		environment.lifecycle().manage(new DataGrabberMain());
	}

}
