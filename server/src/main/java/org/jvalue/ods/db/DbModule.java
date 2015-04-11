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
import org.jvalue.commons.auth.UserRepository;
import org.jvalue.commons.couchdb.CouchDbConfig;
import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.commons.utils.Cache;

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
					.username(couchDbConfig.getAdmin().getUsername())
					.password(couchDbConfig.getAdmin().getPassword())
					.maxConnections(couchDbConfig.getMaxConnections())
					.build());
			DbConnectorFactory connectorFactory = new DbConnectorFactory(couchDbInstance, couchDbConfig.getDbPrefix());

			CouchDbConnector dataSourceConnector = connectorFactory.createConnector(DataSourceRepository.DATABASE_NAME, true);
			bind(CouchDbConnector.class).annotatedWith(Names.named(DataSourceRepository.DATABASE_NAME)).toInstance(dataSourceConnector);

			CouchDbConnector userConnector = connectorFactory.createConnector(UserRepository.DATABASE_NAME, true);
			bind(CouchDbConnector.class).annotatedWith(Names.named(UserRepository.DATABASE_NAME)).toInstance(userConnector);

			bind(DbConnectorFactory.class).toInstance(connectorFactory);
			install(new FactoryModuleBuilder().build(RepositoryFactory.class));

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
