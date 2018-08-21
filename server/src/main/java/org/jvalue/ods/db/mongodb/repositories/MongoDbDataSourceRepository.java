package org.jvalue.ods.db.mongodb.repositories;

import org.bson.Document;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.api.sources.DataSource;
import org.value.commons.mongodb.AbstractMongoDbRepository;
import org.value.commons.mongodb.MongoDbDocument;
import org.value.commons.mongodb.MongoDbDocumentAdaptable;
import org.value.commons.mongodb.MongoDbRepositoryAdapter;

public class MongoDbDataSourceRepository extends MongoDbRepositoryAdapter<
	MongoDbDataSourceRepository.MongoDbDataSourceRepositoryImpl,
	MongoDbDataSourceRepository.MongoDbDataSourceDocument,
	DataSource> implements GenericRepository<DataSource> {

	private static final String DATABASE_NAME = "dataSources";
	private static final String COLLECTION_NAME = "dataSourceCollection";


	public MongoDbDataSourceRepository(DbConnectorFactory connectorFactory) {
		super(new MongoDbDataSourceRepositoryImpl(connectorFactory, DATABASE_NAME, COLLECTION_NAME));
	}


	static class MongoDbDataSourceRepositoryImpl extends AbstractMongoDbRepository<MongoDbDataSourceDocument> implements MongoDbDocumentAdaptable<MongoDbDataSourceDocument, DataSource> {

		protected MongoDbDataSourceRepositoryImpl(DbConnectorFactory connectorFactory, String databaseName, String collectionName) {
			super(connectorFactory, databaseName, collectionName, MongoDbDataSourceDocument.class);
		}


		@Override
		protected MongoDbDataSourceDocument createNewDocument(Document document) {
			return new MongoDbDataSourceDocument(document);
		}


		@Override
		protected String getValueId(MongoDbDataSourceDocument Value) {
			return Value.getValue().getId();
		}


		@Override
		public MongoDbDataSourceDocument createDbDocument(DataSource value) {
			return new MongoDbDataSourceDocument(value);
		}


		@Override
		public String getIdForValue(DataSource value) {
			return value.getId();
		}
	}

	static class MongoDbDataSourceDocument extends MongoDbDocument<DataSource> {
		public MongoDbDataSourceDocument(DataSource valueObject) {
			super(valueObject, DataSource.class);
		}


		public MongoDbDataSourceDocument(Document document) {
			super(document, DataSource.class);
		}

	}
}

