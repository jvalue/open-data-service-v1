package org.jvalue.ods.main;


import com.google.inject.Guice;
import com.google.inject.Injector;

import org.jvalue.ods.configuration.ConfigurationModule;
import org.jvalue.ods.configuration.PegelOnlineConfigurationFactory;
import org.jvalue.ods.data.DataModule;
import org.jvalue.ods.data.DataSourceConfiguration;
import org.jvalue.ods.db.DataRepositoryCache;
import org.jvalue.ods.db.DataSourceRepository;
import org.jvalue.ods.db.DbModule;
import org.jvalue.ods.db.FilterChainReferenceRepository;
import org.jvalue.ods.filter.FilterChainFactory;
import org.jvalue.ods.filter.FilterChainManager;
import org.jvalue.ods.filter.FilterModule;
import org.jvalue.ods.notifications.NotificationsModule;
import org.jvalue.ods.rest.DataApi;
import org.jvalue.ods.rest.DbExceptionMapper;
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
		{
			DataSourceConfiguration pegelonlineConfig = injector.getInstance(PegelOnlineConfigurationFactory.class).createConfiguration();

			// init data repository
			DataRepositoryCache repositoryCache = injector.getInstance(DataRepositoryCache.class);
			repositoryCache.putRepository(pegelonlineConfig.getDataSource().getSourceId(), pegelonlineConfig.getDataRepository());

			// insert data source
			DataSourceRepository sourceRepository = injector.getInstance(DataSourceRepository.class);
			if (sourceRepository.findBySourceId(pegelonlineConfig.getDataSource().getSourceId()).isEmpty()) {
				sourceRepository.add(pegelonlineConfig.getDataSource());
			}

			// insert filter chain
			FilterChainReferenceRepository filterRepository = injector.getInstance(FilterChainReferenceRepository.class);
			if (filterRepository.findByDataSourceId(pegelonlineConfig.getDataSource().getSourceId()).isEmpty()) {
				filterRepository.add(pegelonlineConfig.getFilterChainReference());
			}
			FilterChainManager chainManager = injector.getInstance(FilterChainManager.class);
			FilterChainFactory chainFactory = injector.getInstance(FilterChainFactory.class);
			chainManager.register(chainFactory.createFilterChain(pegelonlineConfig.getFilterChainReference()));
		}


		// start data grabbing
		environment.lifecycle().manage(injector.getInstance(DataGrabberManager.class));
		environment.jersey().register(injector.getInstance(NotificationClientRegistrationApi.class));
		environment.jersey().register(injector.getInstance(DataApi.class));
		environment.jersey().register(new DbExceptionMapper());
	}

}
