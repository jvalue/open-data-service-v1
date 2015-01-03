package org.jvalue.ods.db;


import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.jvalue.ods.notifications.clients.Client;

import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.clientId) emit( null, doc)}")
public final class NotificationClientRepository extends CouchDbRepositorySupport<Client> {

	@Inject
	NotificationClientRepository(CouchDbInstance couchDbInstance, @Assisted String databaseName) {
		super(Client.class, couchDbInstance.createConnector(databaseName, true));
		initStandardDesignDocument();
	}


	@GenerateView
	public Client findByClientId(String clientId) {
		List<Client> clients = queryView("by_clientId", clientId);
		if (clients.isEmpty()) throw new DocumentNotFoundException(clientId);
		if (clients.size() > 1) throw new IllegalStateException("found more than one client for id " + clientId);
		return clients.get(0);
	}

}
