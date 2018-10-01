package org.jvalue.ods.db.generic;

import com.google.inject.AbstractModule;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.jvalue.commons.utils.HttpServiceCheck;
import org.jvalue.commons.utils.Log;
import org.jvalue.ods.db.couchdb.CouchDbModule;
import org.jvalue.ods.db.mongodb.MongoDbModule;
import org.jvalue.ods.main.OdsConfig;

public class DbModule extends AbstractModule {

	private final OdsConfig configuration;
	private final boolean useMongoDb;


	public DbModule(OdsConfig configuration, boolean useMongoDb) {
		this.configuration = configuration;
		this.useMongoDb = useMongoDb;
	}


	@Override
	protected void configure() {

		if (useMongoDb) {
			install(new MongoDbModule(configuration.getMongoDb()));
			Log.info("Using MongoDB!");
		} else {
			install(new CouchDbModule(configuration.getCouchDb()));
			Log.info("Using CouchDB!");
		}

	}
}

