package org.jvalue.ods.db.mongodb.repositories;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.notifications.Client;

public class MongoDbNotificationClientRepository extends AbstractMongoDbRepository<Client> {

	private static final String COLLECTION_NAME = "notificationClientCollection";


	@Inject
	public MongoDbNotificationClientRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(dbConnectorFactory, databaseName, COLLECTION_NAME, Client.class);
	}
}
