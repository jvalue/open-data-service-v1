package org.jvalue.ods.db.mongodb;

import com.google.inject.AbstractModule;
import org.value.commons.mongodb.MongoDbConfig;

public class MongoDbModule extends AbstractModule {

	private final MongoDbConfig mongoDbConfig;

	public MongoDbModule(MongoDbConfig mongoDbConfig) {
		this.mongoDbConfig = mongoDbConfig;
	}

	@Override
	protected void configure() {

	}

}

