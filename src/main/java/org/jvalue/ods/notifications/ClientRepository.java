package org.jvalue.ods.notifications;


import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.jvalue.ods.notifications.clients.Client;

import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.clientId && doc.source) emit( null, doc)}")
public final class ClientRepository extends CouchDbRepositorySupport<Client> {

	public ClientRepository(CouchDbConnector connector) {
		super(Client.class, connector);
	}


	@GenerateView
	public List<Client> findByClientId(String clientId) {
		return queryView("by_clientId");
	}


	@GenerateView
	public List<Client> findBySource(String source) {
		return queryView("by_source");
	}

}
