package org.jvalue.ods.db.couchdb;


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
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.RepositoryFactory;
import org.jvalue.ods.decoupleDatabase.IDataRepository;
import org.jvalue.commons.db.IRepository;

import java.net.MalformedURLException;

public class CouchDbModule extends AbstractModule {

	private final CouchDbConfig couchDbConfig;

	public CouchDbModule(CouchDbConfig couchDbConfig) {
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
			DbConnectorFactory connectorFactory = new CouchDbConnectorFactory(couchDbInstance, couchDbConfig.getDbPrefix());

			CouchDbConnector dataSourceConnector = (CouchDbConnector) connectorFactory.createConnector(DataSourceRepository.DATABASE_NAME, true);
			bind(CouchDbConnector.class).annotatedWith(Names.named(DataSourceRepository.DATABASE_NAME)).toInstance(dataSourceConnector);

			CouchDbConnector userConnector = (CouchDbConnector) connectorFactory.createConnector(UserRepository.DATABASE_NAME, true);
			bind(CouchDbConnector.class).annotatedWith(Names.named(UserRepository.DATABASE_NAME)).toInstance(userConnector);

			bind(DbConnectorFactory.class).toInstance(connectorFactory);

			bind(new TypeLiteral<Cache<IRepository<CouchDbDataView>>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<IRepository<ProcessorReferenceChain>>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<IRepository<Client>>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<IRepository<PluginMetaData>>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<IRepository<JsonNode>>>() { }).in(Singleton.class);


			bind(new TypeLiteral<IRepository<DataSource>>() {}).to(DataSourceRepository.class);
			install(new FactoryModuleBuilder()
				.implement(
					new TypeLiteral<IRepository<CouchDbDataView>>() {
					},
					Names.named(RepositoryFactory.NAME_DATA_VIEW_REPOSITORY),
					DataViewRepository.class)

				.implement(
					new TypeLiteral<IRepository<DataSource>>() {
					},
					Names.named(RepositoryFactory.NAME_DATA_SOURCE_REPOSITORY),
					DataSourceRepository.class)

				.implement(
					new TypeLiteral<IDataRepository<CouchDbDataView, JsonNode>>() {
					},
					Names.named(RepositoryFactory.NAME_DATA_REPOSITORY),
					DataRepository.class)

				.implement(
					new TypeLiteral<IRepository<ProcessorReferenceChain>>() {
					},
					Names.named(RepositoryFactory.NAME_FILTER_CHAIN_REF_REPOSITORY),
					ProcessorChainReferenceRepository.class)

				.implement(
					new TypeLiteral<IRepository<Client>>() {
					},
					Names.named(RepositoryFactory.NAME_NOTIFICATION_CLIENT_REPOSITORY),
					NotificationClientRepository.class)

				.implement(
					new TypeLiteral<IRepository<PluginMetaData>>() {
					},
					Names.named(RepositoryFactory.NAME_PLUGIN_META_DATA_REPOSITORY),
					PluginMetaDataRepository.class)

				.build(RepositoryFactory.class));
		} catch (MalformedURLException mue) {
			throw new RuntimeException(mue);
		}
	}

}
