package org.jvalue.ods.db;


import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.commons.couchdb.test.AbstractRepositoryAdapterTest;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.notifications.GcmClient;

public class NotificationClientRepositoryTest extends AbstractRepositoryAdapterTest<Client> {

	@Override
	protected RepositoryAdapter<?, ?, Client> doCreateAdapter(DbConnectorFactory connectorFactory) {
		return new NotificationClientRepository(connectorFactory, getClass().getSimpleName());
	}


	@Override
	protected Client doCreateValue(String id, String data) {
		return new GcmClient(id, data);
	}

}
