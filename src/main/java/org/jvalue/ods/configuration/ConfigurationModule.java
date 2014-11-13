package org.jvalue.ods.configuration;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public final class ConfigurationModule extends AbstractModule {

	private final static String
			NAME_PEGELONLINE_CONFIGURATION = "PegelOnlineConfiguration",
			NAME_PEGELPORTAL_MV_CONFIGURATION = "PegelPortalMvConfiguration",
			NAME_OSM_CONFIGURATION = "OsmConfiguration";


	@Override
	protected void configure() {
		bind(ConfigurationManager.class).in(Singleton.class);
		bind(PegelOnlineConfiguration.class).annotatedWith(Names.named(NAME_PEGELONLINE_CONFIGURATION));
		bind(PegelPortalMvConfiguration.class).annotatedWith(Names.named(NAME_PEGELPORTAL_MV_CONFIGURATION));
		bind(OsmConfiguration.class).annotatedWith(Names.named(NAME_OSM_CONFIGURATION));
	}

}
