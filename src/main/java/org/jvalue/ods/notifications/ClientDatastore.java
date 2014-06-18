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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.DbUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;



public final class ClientDatastore {

	private static final String DATABASE_NAME = "notifications";
	private static ClientDatastore instance;

	private static final String
		KEY_CLIENTID = "clientId",
		KEY_SOURCE = "source";



	public static ClientDatastore getInstance() {
		if (instance == null) instance = new ClientDatastore();
		return instance;
	}


	private final OdsView getAllClientsView = new OdsView(
					"_design/notifications",
					"getAllClients",
					"function(doc) { emit(doc._id, doc) }");


	private final DbAccessor<JsonNode> dbAccessor;
	private ClientDatastore() {
		this.dbAccessor = DbFactory.createDbAccessor(DATABASE_NAME);
		this.dbAccessor.connect();

		DbUtils.createView(dbAccessor, getAllClientsView);
	}


	public void registerClient(String clientId, String source) {
		if (clientId == null || source == null) 
			throw new NullPointerException("params cannot be null");

		Map<String, String> client = new HashMap<String, String>();
		client.put(KEY_CLIENTID, clientId);
		client.put(KEY_SOURCE, source);

		if (!isClientRegistered(clientId, source)) dbAccessor.insert(client);
	}


	public void unregisterClient(String clientId, String source) { 
		if (clientId == null || source == null) 
			throw new NullPointerException("params cannot be null");

		List<JsonNode> clients = getAllClientsAsJson();
		for (JsonNode client : clients) {
			if (areEquals(client, clientId, source)) dbAccessor.delete(client);
		}

	}


	public void unregisterClient(String clientId) {
		if (clientId == null) throw new NullPointerException("param cannot be null");

		List<JsonNode> clients = getAllClientsAsJson();
		for (JsonNode client : clients) {
			if (client.get(KEY_CLIENTID).asText().equals(clientId))
				dbAccessor.delete(client);
		}


	}


	public boolean isClientRegistered(String clientId, String source) {
		if (clientId == null || source == null) 
			throw new NullPointerException("params cannot be null");

		for (JsonNode client : getAllClientsAsJson())
			if (areEquals(client, clientId, source)) return true;

		return false;
	}

	
	public void updateClientId(String oldId, String newId) {
		if (oldId == null || newId == null)
			throw new NullPointerException("param cannot be null");

		for (JsonNode client : getAllClientsAsJson()) {
			if (client.get(KEY_CLIENTID).asText().equals(oldId)) {
				ObjectNode c = (ObjectNode) client;
				c.put(KEY_CLIENTID, newId);
				dbAccessor.update(c);
			}
		}
	}


	public Map<String,Set<String>> getRegisteredClients() {
		List<JsonNode> clients = getAllClientsAsJson();

		Map<String, Set<String>> ret = new HashMap<String, Set<String>>();

		for (JsonNode node : clients) {
			String source = node.get(KEY_SOURCE).asText();
			String clientId = node.get(KEY_CLIENTID).asText();
			if (!ret.containsKey(source)) ret.put(source, new HashSet<String>());
			ret.get(source).add(clientId);
		}

		return ret;
	}


	private List<JsonNode> getAllClientsAsJson() {
		return dbAccessor.executeDocumentQuery(
				getAllClientsView.getIdPath(),
				getAllClientsView.getViewName(),
				null);
	}


	private boolean areEquals(JsonNode client, String clientId, String source) {
		return client.get(KEY_SOURCE).asText().equals(source) 
				&& client.get(KEY_CLIENTID).asText().equals(clientId);
	}


}
