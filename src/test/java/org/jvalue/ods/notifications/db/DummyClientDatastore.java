package org.jvalue.ods.notifications.db;

import java.util.HashSet;
import java.util.Set;

import org.jvalue.ods.notifications.clients.Client;


public final class DummyClientDatastore implements ClientDatastore {

	private final Set<Client> clients = new HashSet<Client>();

	@Override
	public void add(Client client) {
		clients.add(client);
	}


	@Override
	public void remove(Client client) {
		clients.remove(client);
	}


	@Override
	public boolean contains(Client client) {
		return clients.contains(client);
	}


	@Override
	public Set<Client> getAll() {
		return new HashSet<Client>(clients);
	}

}
