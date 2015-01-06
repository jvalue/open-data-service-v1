package org.jvalue.ods.db;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.ods.notifications.clients.Client;

import java.util.List;

public final class NotificationClientRepository extends RepositoryAdapter<
		NotificationClientRepository.NotificationClientCouchDbRepository,
		NotificationClientRepository.ClientDocument,
		Client> {

	@Inject
	NotificationClientRepository(CouchDbInstance couchDbInstance, @Assisted String databaseName) {
		super(new NotificationClientCouchDbRepository(couchDbInstance, databaseName));
	}


	@View( name = "all", map = "function(doc) { if (doc.value.clientId && doc.value.type) emit(null, doc)}")
	static class NotificationClientCouchDbRepository
			extends CouchDbRepositorySupport<NotificationClientRepository.ClientDocument>
			implements DbDocumentAdaptable<NotificationClientRepository.ClientDocument, Client> {

		public NotificationClientCouchDbRepository(CouchDbInstance couchDbInstance, String databaseName) {
			super(ClientDocument.class, couchDbInstance.createConnector(databaseName, true));
			initStandardDesignDocument();
		}


		@View(name = "by_id", map = "function(doc) { if (doc.value.clientId && doc.value.type) emit(doc.value.clientId, doc._id)}")
		public ClientDocument findById(String clientId) {
			List<ClientDocument> clients = queryView("by_id", clientId);
			if (clients.isEmpty()) throw new DocumentNotFoundException(clientId);
			if (clients.size() > 1) throw new IllegalStateException("found more than one client for id " + clientId);
			return clients.get(0);
		}


		@Override
		public ClientDocument createDbDocument(Client client) {
			return new ClientDocument(client);
		}


		@Override
		public String getIdForValue(Client client) {
			return client.getClientId();
		}

	}


	static final class ClientDocument extends DbDocument<Client> {

		@JsonCreator
		public ClientDocument(
				@JsonProperty("value") Client client) {
			super(client);
		}

	}

}
