package org.jvalue.ods.main;


import com.google.inject.AbstractModule;

public final class ConfigModule extends AbstractModule {

	private final OdsConfig config;

	public  ConfigModule(OdsConfig config) {
		this.config = config;
	}


	@Override
	protected void configure() {
		bind(DataGrabberManager.class);
		// accessor = DbFactory.createDbAccessor("ods");
		// accessor.connect();
		bind(String.class).annotatedWith(GcmApiKey.class).toInstance(config.getGcmApiKey());
		bind(Long.class).annotatedWith(GrabberUpdateInterval.class).toInstance(config.getGrabberUpdateInterval());
	}

}
