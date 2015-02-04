package org.jvalue.ods.db;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.jvalue.common.db.CouchDbConfig;
import org.jvalue.ods.utils.Cache;

import java.net.MalformedURLException;

public class DbModule extends AbstractModule {

	private final CouchDbConfig couchDbConfig;

	public DbModule(CouchDbConfig couchDbConfig) {
		this.couchDbConfig = couchDbConfig;
	}


	@Override
	protected void configure() {
		try {
			CouchDbInstance couchDbInstance = new StdCouchDbInstance(new StdHttpClient.Builder()
					.url(couchDbConfig.getUrl())
					.username(couchDbConfig.getUsername())
					.password(couchDbConfig.getPassword())
					.build());
			CouchDbConnector dataSourceConnector = couchDbInstance.createConnector(DataSourceRepository.DATABASE_NAME, true);

			bind(CouchDbInstance.class).toInstance(couchDbInstance);
			bind(CouchDbConnector.class).annotatedWith(Names.named(DataSourceRepository.DATABASE_NAME)).toInstance(dataSourceConnector);
			install(new FactoryModuleBuilder().build(DbFactory.class));

			bind(DataSourceRepository.class).in(Singleton.class);
			bind(new TypeLiteral<Cache<DataViewRepository>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<ProcessorChainReferenceRepository>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<NotificationClientRepository>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<PluginMetaDataRepository>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<DataRepository>>() { }).in(Singleton.class);
		} catch (MalformedURLException mue) {
			throw new RuntimeException(mue);
		}
	}

}
