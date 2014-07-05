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

import org.jvalue.ods.notifications.ClientDatastore;


final class CachedClientDatastore implements ClientDatastore {

	private final Map<String, Set<String>> clients = new HashMap<String,  Set<String>>();
	private final ClientDatastore store;
	public CachedClientDatastore(ClientDatastore store) {
		if (store == null) throw new NullPointerException("param cannot be null");
		this.store = store;

		clients.putAll(store.getRegisteredClients());
	}


	@Override
	public void registerClient(String clientId, String source) {
		store.registerClient(clientId, source);

		if (!clients.containsKey(source)) clients.put(source, new HashSet<String>());
		clients.get(source).add(clientId);
	}


	@Override
	public void unregisterClient(String clientId, String source) {
		store.unregisterClient(clientId, source);

		if (!isClientRegistered(clientId, source)) return;
		clients.get(source).remove(clientId);
		if (clients.get(source).size() == 0) clients.remove(source);
	}


	@Override
	public void unregisterClient(String clientId) {
		store.unregisterClient(clientId);

		for (String source : clients.keySet()) unregisterClient(clientId, source);
	}


	@Override
	public boolean isClientRegistered(String clientId, String source) {
		return clients.containsKey(source) && clients.get(source).contains(clientId);
	}


	@Override
	public void updateClientId(String oldId, String newId) {
		store.updateClientId(oldId, newId);

		for (String source : clients.keySet()) {
			Set<String> ids = clients.get(source);
			if (ids.contains(oldId)) {
				ids.remove(oldId);
				ids.add(newId);
			}
		}
	}


	@Override
	public Map<String,Set<String>> getRegisteredClients() {
		Map<String, Set<String>> ret = new HashMap<String, Set<String>>();
		for (String source : clients.keySet()) {
			ret.put(source, new HashSet<String>(clients.get(source)));
		}
		return ret;
	}


	@Override
	public void removeAllClients() {
		store.removeAllClients();

		clients.clear();
	}

}
