package org.jvalue.ods.db.mongodb.repositories;

import com.google.inject.assistedinject.Assisted;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.notifications.Client;

import java.util.List;

public class MongoDbNotificationClientRepository extends AbstractMongoDbRepository<Client>{
	public MongoDbNotificationClientRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(dbConnectorFactory, databaseName);
	}

	@Override
	protected Class<?> getEntityType() {
		return Client.class;
	}


}
