package org.jvalue.ods.db.mongodb;

import org.junit.Ignore;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.mongodb.test.AbstractRepositoryTestBase;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.db.mongodb.repositories.MongoDbNotificationClientRepository;

@Ignore
public class MongoDbNotificationClientRepositoryTest extends AbstractRepositoryTestBase<Client> {
	@Override
	protected GenericRepository<Client> doCreateRepository(DbConnectorFactory connectorFactory) {
		return new MongoDbNotificationClientRepository(connectorFactory, getClass().getSimpleName());
	}


	@Override
	protected Client doCreateValue(String id, String data) {
		return new GcmClient(id, data);
	}


	@Override
	protected void doDeleteDatabase(DbConnectorFactory dbConnectorFactory) {
		dbConnectorFactory.doDeleteDatabase(getClass().getSimpleName());
	}
}
