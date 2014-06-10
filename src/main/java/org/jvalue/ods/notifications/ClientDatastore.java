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
package org.jvalue.ods.notifications;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



public final class ClientDatastore {

	private static final ClientDatastore instance = new ClientDatastore();
	public static ClientDatastore getInstance() {
		return instance;
	}


	private final Map<String,Set<String>> registeredClients = new HashMap<String,Set<String>>();

	private ClientDatastore() { }


	public void registerClient(String clientId, String source) {
		if (clientId == null || source == null) 
			throw new NullPointerException("params cannot be null");

		Set<String> clients = registeredClients.get(source);
		if (clients == null) {
			clients = new HashSet<String>();
			registeredClients.put(source, clients);
		}
		clients.add(clientId);
	}


	public boolean unregisterClient(String clientId, String source) { 
		if (clientId == null || source == null) 
			throw new NullPointerException("params cannot be null");

		Set<String> clients = registeredClients.get(source);
		boolean ret = clients.remove(clientId);
		if (clients.size() == 0) registeredClients.remove(source);
		return ret;
	}


	public boolean unregisterClient(String clientId) {
		if (clientId == null) throw new NullPointerException("param cannot be null");

		boolean modified = false;

		Iterator<Map.Entry<String, Set<String>>> iter = registeredClients.entrySet().iterator();
		while (iter.hasNext()) {
			Set<String> clients = iter.next().getValue();
			if (clients.contains(clientId)) {
				modified = true;
				clients.remove(clientId);
				if (clients.size() == 0) iter.remove();
			}
		}

		return modified;
	}


	public boolean isClientRegistered(String clientId, String source) {
		if (clientId == null || source == null) 
			throw new NullPointerException("params cannot be null");

		if (registeredClients.get(source) == null) return false;
		else return registeredClients.get(source).contains(clientId);
	}

	
	public void updateClientId(String oldId, String newId) {
		for (Set<String> clients : registeredClients.values()) {
			if (clients.contains(oldId)) {
				clients.remove(oldId);
				clients.add(newId);
			}
		}
	}


	public Map<String,Set<String>> getRegisteredClients() {
		Map<String, Set<String>> ret = new HashMap<String, Set<String>>();
		for (Map.Entry<String, Set<String>> e : registeredClients.entrySet()) {
			ret.put(e.getKey(), new HashSet<String>(e.getValue()));
		}
		return ret;
	}

}
