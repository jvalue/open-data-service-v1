package org.jvalue.ods.db;


import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.jvalue.ods.notifications.clients.Client;

import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.clientId && doc.source) emit( null, doc)}")
public final class NotificationClientRepository extends CouchDbRepositorySupport<Client> {

	static final String DATABASE_NAME = "notificationClients";

	@Inject
	NotificationClientRepository(@Named(DATABASE_NAME) CouchDbConnector connector) {
		super(Client.class, connector);
		initStandardDesignDocument();
	}


	@GenerateView
	public List<Client> findByClientId(String clientId) {
		return queryView("by_clientId", clientId);
	}


	@GenerateView
	public List<Client> findBySource(String source) {
		return queryView("by_source", source);
	}

}
