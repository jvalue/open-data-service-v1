package org.jvalue.ods.db;


import com.fasterxml.jackson.databind.JsonNode;
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
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.decoupleDatabase.IDataRepository;
import org.jvalue.ods.decoupleDatabase.IRepository;
import org.jvalue.ods.decoupleDatabase.couchdb.wrapper.*;

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
//			install(new FactoryModuleBuilder().build(RepositoryFactory.class));

			bind(new TypeLiteral<Cache<IRepository<DataView>>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<IRepository<ProcessorReferenceChain>>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<IRepository<Client>>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<IRepository<PluginMetaData>>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<IRepository<JsonNode>>>() { }).in(Singleton.class);

//			bind(new TypeLiteral<IRepository<DataView>>() {}).to(CouchDbDataViewRepositoryWrapper.class);
			bind(new TypeLiteral<IRepository<DataSource>>() {}).to(CouchDbDataSourceRepositoryWrapper.class);
//			bind(new TypeLiteral<IDataRepository<JsonNode>>() {}).to(CouchDbDataRepositoryWrapper.class);
//			bind(new TypeLiteral<IRepository<ProcessorReferenceChain>>() {}).to(CouchDbProcessorChainReferenceRepositoryWrapper.class);
//			bind(new TypeLiteral<IRepository<Client>>() {}).to(CouchDbNotificationClientRepositoryWrapper.class);
//			bind(new TypeLiteral<IRepository<PluginMetaData>>() {}).to(CouchDbPluginMetaDataRepositoryWrapper.class);

			install(new FactoryModuleBuilder()
				.implement(
					new TypeLiteral<IRepository<DataView>>() {
					},
					Names.named(RepositoryFactory.NAME_DATA_VIEW_REPOSITORY),
					CouchDbDataViewRepositoryWrapper.class)

				.implement(
					new TypeLiteral<IRepository<DataSource>>() {
					},
					Names.named(RepositoryFactory.NAME_DATA_SOURCE_REPOSITORY),
					CouchDbDataSourceRepositoryWrapper.class)

				.implement(
					new TypeLiteral<IDataRepository<JsonNode>>() {
					},
					Names.named(RepositoryFactory.NAME_DATA_REPOSITORY),
					CouchDbDataRepositoryWrapper.class)

				.implement(
					new TypeLiteral<IRepository<ProcessorReferenceChain>>() {
					},
					Names.named(RepositoryFactory.NAME_FILTER_CHAIN_REF_REPOSITORY),
					CouchDbProcessorChainReferenceRepositoryWrapper.class)

				.implement(
					new TypeLiteral<IRepository<Client>>() {
					},
					Names.named(RepositoryFactory.NAME_NOTIFICATION_CLIENT_REPOSITORY),
					CouchDbNotificationClientRepositoryWrapper.class)

				.implement(
					new TypeLiteral<IRepository<PluginMetaData>>() {
					},
					Names.named(RepositoryFactory.NAME_PLUGIN_META_DATA_REPOSITORY),
					CouchDbPluginMetaDataRepositoryWrapper.class)

				.build(RepositoryFactory.class));
		} catch (MalformedURLException mue) {
			throw new RuntimeException(mue);
		}
	}

}
