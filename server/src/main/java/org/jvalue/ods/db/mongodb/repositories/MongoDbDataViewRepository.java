package org.jvalue.ods.db.mongodb.repositories;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.bson.Document;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.value.commons.mongodb.AbstractMongoDbRepository;
import org.value.commons.mongodb.MongoDbDocument;
import org.value.commons.mongodb.MongoDbDocumentAdaptable;
import org.value.commons.mongodb.MongoDbRepositoryAdapter;

public class MongoDbDataViewRepository extends MongoDbRepositoryAdapter<
	MongoDbDataViewRepository.MongoDbDataViewRepositoryImpl,
	MongoDbDataViewRepository.MongoDbDataViewDocument,
	CouchDbDataView> implements GenericRepository<CouchDbDataView> {

	private static final String COLLECTION_NAME = "dataViewCollection";


	@Inject
	public MongoDbDataViewRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(new MongoDbDataViewRepositoryImpl(dbConnectorFactory, databaseName, COLLECTION_NAME));
	}


	static class MongoDbDataViewRepositoryImpl extends AbstractMongoDbRepository<MongoDbDataViewDocument> implements MongoDbDocumentAdaptable<MongoDbDataViewDocument, CouchDbDataView> {

		protected MongoDbDataViewRepositoryImpl(DbConnectorFactory connectorFactory, String databaseName, String collectionName) {
			super(connectorFactory, databaseName, collectionName, MongoDbDataViewDocument.class);
		}


		@Override
		protected MongoDbDataViewDocument createNewDocument(Document document) {
			return new MongoDbDataViewDocument(document);
		}


		@Override
		protected String getValueId(MongoDbDataViewDocument Value) {
			return Value.getValue().getId();
		}


		@Override
		public MongoDbDataViewDocument createDbDocument(CouchDbDataView value) {
			return new MongoDbDataViewDocument(value);
		}


		@Override
		public String getIdForValue(CouchDbDataView value) {
			return value.getId();
		}
	}

	static class MongoDbDataViewDocument extends MongoDbDocument<CouchDbDataView> {
		public MongoDbDataViewDocument(CouchDbDataView valueObject) {
			super(valueObject, CouchDbDataView.class);
		}


		public MongoDbDataViewDocument(Document document) {
			super(document, CouchDbDataView.class);
		}

	}

}
