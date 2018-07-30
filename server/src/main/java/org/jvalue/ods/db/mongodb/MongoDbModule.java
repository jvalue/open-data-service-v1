package org.jvalue.ods.db.mongodb;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.factories.AuthRepositoryFactory;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.generic.RepositoryFactory;
import org.jvalue.ods.db.couchdb.repositories.*;
import org.jvalue.ods.db.generic.DataSourceFactory;
import org.jvalue.ods.db.mongodb.repositories.*;
import org.value.commons.mongodb.MongoDbConfig;

public class MongoDbModule extends AbstractModule {

	private final MongoDbConfig mongoDbConfig;


	public MongoDbModule(MongoDbConfig mongoDbConfig) {
		this.mongoDbConfig = mongoDbConfig;
	}


	@Override
	protected void configure() {

		MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoDbConfig.getUrl()));

		MongoDbConnectorFactory connectorFactory = new MongoDbConnectorFactory(mongoClient, mongoDbConfig.getDbPrefix());

		bind(DbConnectorFactory.class).toInstance(connectorFactory);
		bind(DataSourceFactory.class).to(MongoDbDataSourceFactory.class);
		bind(AuthRepositoryFactory.class).to(MongoDbAuthRepositoryFactory.class);

		bind(new TypeLiteral<Cache<GenericRepository<CouchDbDataView>>>() {}).in(Singleton.class);
		bind(new TypeLiteral<Cache<GenericRepository<ProcessorReferenceChain>>>() {}).in(Singleton.class);
		bind(new TypeLiteral<Cache<GenericRepository<Client>>>() {}).in(Singleton.class);
		bind(new TypeLiteral<Cache<GenericRepository<PluginMetaData>>>() {}).in(Singleton.class);

		bind(new TypeLiteral<Cache<MongoDbDataRepository>>() {}).in(Singleton.class);


		install(new FactoryModuleBuilder()
			.implement(
				new TypeLiteral<GenericRepository<CouchDbDataView>>() {
				},
				Names.named(RepositoryFactory.NAME_DATA_VIEW_REPOSITORY),
				MongoDbDataViewRepository.class)

			.implement(
				new TypeLiteral<GenericDataRepository<CouchDbDataView, JsonNode>>() {
				},
				Names.named(RepositoryFactory.NAME_DATA_REPOSITORY),
				MongoDbDataRepository.class)

			.implement(
				new TypeLiteral<GenericRepository<ProcessorReferenceChain>>() {
				},
				Names.named(RepositoryFactory.NAME_FILTER_CHAIN_REF_REPOSITORY),
				MongoDbProcessorChainReferenceRepository.class)

			.implement(
				new TypeLiteral<GenericRepository<Client>>() {
				},
				Names.named(RepositoryFactory.NAME_NOTIFICATION_CLIENT_REPOSITORY),
				MongoDbNotificationClientRepository.class)

			.implement(
				new TypeLiteral<GenericRepository<PluginMetaData>>() {
				},
				Names.named(RepositoryFactory.NAME_PLUGIN_META_DATA_REPOSITORY),
				MongoDbPluginMetaDataRepository.class)

			.build(RepositoryFactory.class));

	}
}

