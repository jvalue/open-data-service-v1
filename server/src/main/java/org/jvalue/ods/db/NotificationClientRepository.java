package org.jvalue.ods.db;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.commons.couchdb.DbDocument;
import org.jvalue.commons.couchdb.DbDocumentAdaptable;
import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.ods.api.notifications.Client;

import java.util.List;

public final class NotificationClientRepository extends RepositoryAdapter<
		NotificationClientRepository.NotificationClientCouchDbRepository,
		NotificationClientRepository.ClientDocument,
		Client> {

	private static final String DOCUMENT_ID = "doc.value.id != null && doc.value.type != null";

	@Inject
	NotificationClientRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(new NotificationClientCouchDbRepository(dbConnectorFactory.createConnector(databaseName, true)));
	}


	@View( name = "all", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(null, doc)}")
	static class NotificationClientCouchDbRepository
			extends CouchDbRepositorySupport<NotificationClientRepository.ClientDocument>
			implements DbDocumentAdaptable<ClientDocument, Client> {

		public NotificationClientCouchDbRepository(CouchDbConnector connector) {
			super(ClientDocument.class, connector);
			initStandardDesignDocument();
		}


		@View(name = "by_id", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(doc.value.id, doc._id)}")
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
			return client.getId();
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
