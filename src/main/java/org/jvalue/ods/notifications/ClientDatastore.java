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

		Set<String> sources = registeredClients.get(clientId);
		if (sources == null) {
			sources = new HashSet<String>();
			registeredClients.put(clientId, sources);
		}
		sources.add(source);
	}


	public boolean unregisterClient(String clientId, String source) { 
		if (clientId == null || source == null) 
			throw new NullPointerException("params cannot be null");

		Set<String> sources = registeredClients.get(clientId);
		boolean ret = sources.remove(source);
		if (sources.size() == 0) registeredClients.remove(clientId);
		return ret;
	}


	public boolean isClientRegistered(String clientId, String source) {
		if (clientId == null || source == null) 
			throw new NullPointerException("params cannot be null");

		if (registeredClients.get(clientId) == null) return false;
		else return registeredClients.get(clientId).contains(source);
	}


	public Map<String,Set<String>> getRegisteredClients() {
		Map<String, Set<String>> ret = new HashMap<String, Set<String>>();
		for (Map.Entry<String, Set<String>> e : ret.entrySet()) {
			ret.put(e.getKey(), new HashSet<String>(e.getValue()));
		}
		return ret;
	}

}
