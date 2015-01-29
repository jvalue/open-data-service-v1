package org.jvalue.ods.db;


import org.ektorp.CouchDbInstance;
import org.jvalue.common.db.RepositoryAdapter;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.notifications.GcmClient;

public class NotificationClientRepositoryTest extends AbstractRepositoryAdapterTest<Client> {

	public NotificationClientRepositoryTest() {
		super(NotificationClientRepositoryTest.class.getSimpleName());
	}


	@Override
	protected RepositoryAdapter<?, ?, Client> doCreateAdapter(CouchDbInstance couchDbInstance, String databaseName) {
		return new NotificationClientRepository(couchDbInstance, databaseName);
	}


	@Override
	protected Client doCreateValue(String id, String data) {
		return new GcmClient(id, data);
	}

}
