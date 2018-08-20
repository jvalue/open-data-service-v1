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

public class MongoDbNotificationClientRepository extends AbstractMongoDbRepository<Client> {

	private static final String COLLECTION_NAME = "notificationClientCollection";


	@Inject
	public MongoDbNotificationClientRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(dbConnectorFactory, databaseName, COLLECTION_NAME, Client.class);
	}


	@Override
	protected String getValueId(Client Value) {
		return Value.getId();
	}
}
