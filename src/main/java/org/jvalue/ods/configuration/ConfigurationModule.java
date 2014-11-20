package org.jvalue.ods.configuration;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public final class ConfigurationModule extends AbstractModule {

	public final static String
			NAME_PEGELONLINE_CONFIGURATION = "PegelOnlineConfiguration",
			NAME_PEGELPORTAL_MV_CONFIGURATION = "PegelPortalMvConfiguration",
			NAME_OSM_CONFIGURATION = "OsmConfiguration";


	@Override
	protected void configure() {

		bind(ConfigurationManager.class).in(Singleton.class);
		bind(Configuration.class)
				.annotatedWith(Names.named(NAME_PEGELONLINE_CONFIGURATION))
				.to(PegelOnlineConfiguration.class);
		/*
		bind(Configuration.class)
				.annotatedWith(Names.named(NAME_PEGELPORTAL_MV_CONFIGURATION))
				.to(PegelPortalMvConfiguration.class);
		bind(Configuration.class)
				.annotatedWith(Names.named(NAME_OSM_CONFIGURATION))
				.to(OsmConfiguration.class);
				*/
	}

}
