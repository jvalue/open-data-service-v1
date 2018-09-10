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
import org.jvalue.commons.couchdb.CouchDbConfig;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.factories.AuthRepositoryFactory;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.couchdb.repositories.*;
import org.jvalue.ods.db.generic.DataSourceRepositoryFactory;
import org.jvalue.ods.db.generic.RepositoryFactory;
import org.jvalue.ods.api.views.generic.TransformationFunction;

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

			bind(DbConnectorFactory.class).toInstance(connectorFactory);
			bind(DataSourceRepositoryFactory.class).to(CouchDbDataSourceRepositoryFactory.class);
			bind(AuthRepositoryFactory.class).to(CouchDbAuthRepositoryFactory.class);

			bind(new TypeLiteral<Cache<GenericRepository<CouchDbDataView>>>() {
			}).in(Singleton.class);
			bind(new TypeLiteral<Cache<GenericRepository<ProcessorReferenceChain>>>() {
			}).in(Singleton.class);
			bind(new TypeLiteral<Cache<GenericRepository<Client>>>() {
			}).in(Singleton.class);
			bind(new TypeLiteral<Cache<GenericRepository<PluginMetaData>>>() {
			}).in(Singleton.class);
			bind(new TypeLiteral<Cache<DataRepository>>() {
			}).in(Singleton.class);


			install(new FactoryModuleBuilder()
				.implement(
					new TypeLiteral<GenericRepository<CouchDbDataView>>() {
					},
					Names.named(RepositoryFactory.NAME_DATA_VIEW_REPOSITORY),
					DataViewRepository.class)

				.implement(
					new TypeLiteral<GenericDataRepository<JsonNode>>() {
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

				.implement(
					new TypeLiteral<GenericRepository<TransformationFunction>>() {
					},
					Names.named(RepositoryFactory.NAME_TRANSFORMATION_FUNCTION_REPOSITORY),
					TransformationFunctionRepository.class)

				.build(RepositoryFactory.class));

		} catch (MalformedURLException mue) {
			throw new RuntimeException(mue);
		}
	}

}
