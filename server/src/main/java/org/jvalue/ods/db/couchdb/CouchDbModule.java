package org.jvalue.ods.db.couchdb;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.jvalue.commons.auth.*;
import org.jvalue.commons.couchdb.CouchDbConfig;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.generic.DataSourceFactory;
import org.jvalue.ods.db.couchdb.repositories.*;

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

//			CouchDbConnector dataSourceConnector = (CouchDbConnector) connectorFactory.createConnector(DataSourceRepository.DATABASE_NAME, true);
//			bind(CouchDbConnector.class).annotatedWith(Names.named(RepositoryAdapter.COUCHDB_CONNECTOR_FACTORY)).toInstance(dataSourceConnector);

//			CouchDbConnector userConnector = (CouchDbConnector) connectorFactory.createConnector(GenericUserRepository.DATABASE_NAME, true);
//			bind(CouchDbConnector.class).annotatedWith(Names.named(GenericUserRepository.DATABASE_NAME)).toInstance(userConnector);

			bind(DbConnectorFactory.class).toInstance(connectorFactory);
			bind(DataSourceFactory.class).to(CouchDbDataSourceFactory.class);
			bind(BasicCredentialsRepositoryFactory.class).to(CouchDbBasicCredentialsRepositoryFactory.class);
			bind(UserRepositoryFactory.class).to(CouchDbUserRepositoryFactory.class);

			bind(new TypeLiteral<Cache<GenericRepository<CouchDbDataView>>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<GenericRepository<ProcessorReferenceChain>>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<GenericRepository<Client>>>() { }).in(Singleton.class);
			bind(new TypeLiteral<Cache<GenericRepository<PluginMetaData>>>() { }).in(Singleton.class);
//			bind(new TypeLiteral<Cache<GenericRepository<JsonNode>>>() { }).in(Singleton.class);


			install(new FactoryModuleBuilder()
				.implement(
					new TypeLiteral<GenericRepository<CouchDbDataView>>() {
					},
					Names.named(RepositoryFactory.NAME_DATA_VIEW_REPOSITORY),
					DataViewRepository.class)

				.implement(
					new TypeLiteral<GenericRepository<DataSource>>() {
					},
					Names.named(RepositoryFactory.NAME_DATA_SOURCE_REPOSITORY),
					DataSourceRepository.class)

				.implement(
					new TypeLiteral<GenericDataRepository<CouchDbDataView, JsonNode>>() {
					},
					Names.named(RepositoryFactory.NAME_DATA_REPOSITORY),
					DataRepository.class)

				.implement(
					new TypeLiteral<GenericRepository<ProcessorReferenceChain>>() {
					},
					Names.named(RepositoryFactory.NAME_FILTER_CHAIN_REF_REPOSITORY),
					ProcessorChainReferenceRepository.class)

				.implement(
					new TypeLiteral<GenericRepository<Client>>() {
					},
					Names.named(RepositoryFactory.NAME_NOTIFICATION_CLIENT_REPOSITORY),
					NotificationClientRepository.class)

				.implement(
					new TypeLiteral<GenericRepository<PluginMetaData>>() {
					},
					Names.named(RepositoryFactory.NAME_PLUGIN_META_DATA_REPOSITORY),
					PluginMetaDataRepository.class)

				.build(RepositoryFactory.class));

		} catch (MalformedURLException mue) {
			throw new RuntimeException(mue);
		}
	}

}
