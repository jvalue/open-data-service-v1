package org.jvalue.ods.db.mongodb.repositories;

import com.google.inject.assistedinject.Assisted;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.value.commons.mongodb.AbstractMongoDbRepository;

import javax.inject.Inject;

public class MongoDbPluginMetaDataRepository extends AbstractMongoDbRepository<PluginMetaData> {

	private static final String COLLECTION_NAME = "pluginMetaDataCollection";


	@Inject
	public MongoDbPluginMetaDataRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(dbConnectorFactory, databaseName, COLLECTION_NAME, PluginMetaData.class);
	}


	@Override
	protected String getValueId(PluginMetaData Value) {
		return Value.getId();
	}
}
