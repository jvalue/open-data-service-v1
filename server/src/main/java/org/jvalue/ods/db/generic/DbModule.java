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
			assertMongoDbIsReady();
			install(new MongoDbModule(configuration.getMongoDb()));
			Log.info("Using MongoDB!");
		} else {
			assertCouchDbIsReady();
			install(new CouchDbModule(configuration.getCouchDb()));
			Log.info("Using CouchDB!");
		}

	}


	private void assertCouchDbIsReady() {
		if (!HttpServiceCheck.check(HttpServiceCheck.COUCHDB_URL)) {
			throw new RuntimeException("CouchDB service is not ready [" + HttpServiceCheck.COUCHDB_URL + "]");
		}

	}


	private void assertMongoDbIsReady() {
		MongoClient mongoClient = new MongoClient(new MongoClientURI(configuration.getMongoDb().getUrl()));
		int retryCounter = 50;
		do {
			try {
				mongoClient.getAddress();
				return;
			} catch (Exception e) {
				Log.error("MongoDb is not available.");
			} finally {
				mongoClient.close();
			}
			--retryCounter;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (retryCounter > 0);

		throw new RuntimeException("MongoDb is not available");
	}
}

