/*  Open Data Service
    Copyright (C) 2013  Tsysin Konstantin, Reischl Patrick

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
 */
package org.jvalue.ods.notifications.db;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jvalue.ods.notifications.clients.Client;


final class CachedClientDatastore implements ClientDatastore {

	private final Map<String, Client> clients = new HashMap<String, Client>();
	private final ClientDatastore store;

	public CachedClientDatastore(ClientDatastore store) {
		if (store == null) throw new NullPointerException("param cannot be null");
		this.store = store;

		for (Client client : store.getAll()) clients.put(client.getClientId(), client);
	}


	@Override
	public void add(Client client) {
		store.add(client);

		clients.put(client.getClientId(), client);
	}


	@Override
	public void remove(String clientId) {
		store.remove(clientId);

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
