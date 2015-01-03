package org.jvalue.ods.configuration;


import com.google.inject.AbstractModule;

public final class ConfigurationModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(PegelPortalMvConfigurationFactory.class);
		/*
		bind(Configuration.class)
				.annotatedWith(Names.named(NAME_OSM_CONFIGURATION))
				.to(OsmConfiguration.class);
				*/
	}

}
