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

import java.util.HashSet;
import java.util.Set;

import org.jvalue.ods.notifications.Client;
import org.jvalue.ods.notifications.ClientDatastore;


final class CachedClientDatastore implements ClientDatastore {

	private final Set<Client> clients = new HashSet<Client>();
	private final ClientDatastore store;

	public CachedClientDatastore(ClientDatastore store) {
		if (store == null) throw new NullPointerException("param cannot be null");
		this.store = store;

		clients.addAll(store.getRegisteredClients());
	}


	@Override
	public void registerClient(Client client) {
		store.registerClient(client);

		clients.add(client);
	}


	@Override
	public void unregisterClient(Client client) {
		store.unregisterClient(client);

		clients.remove(client);
	}


	@Override
	public boolean isClientRegistered(Client client) {
		return clients.contains(client);
	}


	@Override
	public Set<Client> getRegisteredClients() {
		return new HashSet<Client>(clients);
	}

}
