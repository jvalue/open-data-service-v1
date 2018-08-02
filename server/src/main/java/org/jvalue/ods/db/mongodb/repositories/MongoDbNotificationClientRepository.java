package org.jvalue.ods.db.mongodb.repositories;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.bson.Document;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.api.notifications.Client;
import org.value.commons.mongodb.AbstractMongoDbRepository;
import org.value.commons.mongodb.MongoDbDocument;
import org.value.commons.mongodb.MongoDbDocumentAdaptable;
import org.value.commons.mongodb.MongoDbRepositoryAdapter;

public class MongoDbNotificationClientRepository extends MongoDbRepositoryAdapter<
	MongoDbNotificationClientRepository.MongoDbNotificationClientRepositoryImpl,
	MongoDbNotificationClientRepository.MongoDbNotificationClientDocument,
	Client> implements GenericRepository<Client> {

	private static final String COLLECTION_NAME = "notificationClientCollection";


	@Inject
	public MongoDbNotificationClientRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(new MongoDbNotificationClientRepositoryImpl(dbConnectorFactory, databaseName, COLLECTION_NAME));
	}


	static class MongoDbNotificationClientRepositoryImpl extends AbstractMongoDbRepository<MongoDbNotificationClientDocument> implements MongoDbDocumentAdaptable<MongoDbNotificationClientDocument, Client> {

		protected MongoDbNotificationClientRepositoryImpl(DbConnectorFactory connectorFactory, String databaseName, String collectionName) {
			super(connectorFactory, databaseName, collectionName, MongoDbNotificationClientDocument.class);
		}


		@Override
		protected MongoDbNotificationClientDocument createNewDocument(Document document) {
			return new MongoDbNotificationClientDocument(document);
		}


		@Override
		protected String getValueId(MongoDbNotificationClientDocument Value) {
			return Value.getValue().getId();
		}


		@Override
		public MongoDbNotificationClientDocument createDbDocument(Client value) {
			return new MongoDbNotificationClientDocument(value);
		}


		@Override
		public String getIdForValue(Client value) {
			return value.getId();
		}
	}

	static class MongoDbNotificationClientDocument extends MongoDbDocument<Client> {
		public MongoDbNotificationClientDocument(Client valueObject) {
			super(valueObject, Client.class);
		}


		public MongoDbNotificationClientDocument(Document document) {
			super(document, Client.class);
		}
	}
}
