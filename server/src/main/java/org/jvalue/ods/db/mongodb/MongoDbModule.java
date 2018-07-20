package org.jvalue.ods.db.mongodb;

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
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.RepositoryFactory;
import org.jvalue.ods.db.couchdb.*;
import org.jvalue.ods.decoupleDatabase.IDataRepository;
import org.jvalue.commons.db.IRepository;
import org.value.commons.mongodb.MongoDbConfig;

import java.net.MalformedURLException;

public class MongoDbModule extends AbstractModule {

	private final MongoDbConfig mongoDbConfig;

	public MongoDbModule(MongoDbConfig mongoDbConfig) {
		this.mongoDbConfig = mongoDbConfig;
	}

	@Override
	protected void configure() {

	}

}

