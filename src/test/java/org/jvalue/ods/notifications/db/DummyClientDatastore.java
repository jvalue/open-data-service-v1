package org.jvalue.ods.notifications.db;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jvalue.ods.notifications.clients.Client;


public final class DummyClientDatastore implements ClientDatastore {

	private final Map<String, Client> clients = new HashMap<String, Client>();

	@Override
	public void add(Client client) {
		clients.put(client.getClientId(), client);
	}


	@Override
	public void remove(String clientId) {
		clients.remove(clientId);
	}


	@Override
	public boolean contains(String clientId) {
		return clients.containsKey(clientId);
	}


	@Override
	public Set<Client> getAll() {
		return new HashSet<Client>(clients.values());
	}

}
