package org.jvalue.ods.db.mongodb.repositories;

import com.google.inject.assistedinject.Assisted;
import org.bson.Document;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.value.commons.mongodb.AbstractMongoDbRepository;
import org.value.commons.mongodb.MongoDbDocument;
import org.value.commons.mongodb.MongoDbDocumentAdaptable;
import org.value.commons.mongodb.MongoDbRepositoryAdapter;

import javax.inject.Inject;

public class MongoDbPluginMetaDataRepository extends MongoDbRepositoryAdapter<
	MongoDbPluginMetaDataRepository.MongoDbPluginMetaDataRepositoryImpl,
	MongoDbPluginMetaDataRepository.MongoDbPluginMetaDataDocument,
	PluginMetaData> implements GenericRepository<PluginMetaData> {

	private static final String COLLECTION_NAME = "pluginMetaDataCollection";


	@Inject
	public MongoDbPluginMetaDataRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(new MongoDbPluginMetaDataRepositoryImpl(dbConnectorFactory, databaseName, COLLECTION_NAME));
	}


	static class MongoDbPluginMetaDataRepositoryImpl extends AbstractMongoDbRepository<MongoDbPluginMetaDataDocument> implements MongoDbDocumentAdaptable<MongoDbPluginMetaDataDocument, PluginMetaData> {

		protected MongoDbPluginMetaDataRepositoryImpl(DbConnectorFactory connectorFactory, String databaseName, String collectionName) {
			super(connectorFactory, databaseName, collectionName, MongoDbPluginMetaDataDocument.class);
		}


		@Override
		protected MongoDbPluginMetaDataDocument createNewDocument(Document document) {
			return null;
		}


		@Override
		protected String getValueId(MongoDbPluginMetaDataDocument Value) {
			return Value.getValue().getId();
		}


		@Override
		public MongoDbPluginMetaDataDocument createDbDocument(PluginMetaData value) {
			return null;
		}


		@Override
		public String getIdForValue(PluginMetaData value) {
			return null;
		}
	}

	static class MongoDbPluginMetaDataDocument extends MongoDbDocument<PluginMetaData> {
		public MongoDbPluginMetaDataDocument(PluginMetaData valueObject) {
			super(valueObject, PluginMetaData.class);
		}


		public MongoDbPluginMetaDataDocument(Document document) {
			super(document, PluginMetaData.class);
		}

	}
}
