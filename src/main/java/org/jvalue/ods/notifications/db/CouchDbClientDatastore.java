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
import java.util.List;
import java.util.Set;

import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.DbUtils;
import org.jvalue.ods.notifications.Client;
import org.jvalue.ods.notifications.ClientDatastore;
import org.jvalue.ods.utils.Assert;

import com.fasterxml.jackson.databind.JsonNode;



final class CouchDbClientDatastore implements ClientDatastore {

	private static final String DATABASE_NAME = "notifications";


	private final ClientToMapAdapter clientToMapAdapter = new ClientToMapAdapter();
	private final OdsView getAllClientsView = new OdsView(
					"_design/notifications",
					"getAllClients",
					"function(doc) { emit(doc._id, doc) }");
	private final DbAccessor<JsonNode> dbAccessor;

	CouchDbClientDatastore() {
		this.dbAccessor = DbFactory.createDbAccessor(DATABASE_NAME);
		this.dbAccessor.connect();

		DbUtils.createView(dbAccessor, getAllClientsView);
	}


	@Override
	public void registerClient(Client client) {
		Assert.assertNotNull(client);

		if (!isClientRegistered(client)) {
			dbAccessor.insert(client.accept(clientToMapAdapter, null));
		}
	}


	@Override
	public void unregisterClient(Client client) {
		Assert.assertNotNull(client);

		List<JsonNode> clients = getAllClientsAsJson();
		for (JsonNode node : clients) {
			if (JsonNodeToClientAdapter.toClient(node).equals(client)) {
				dbAccessor.delete(node);
			}
		}
	}


	@Override
	public boolean isClientRegistered(Client client) {
		Assert.assertNotNull(client);

		return getRegisteredClients().contains(client);
	}


	@Override
	public Set<Client> getRegisteredClients() {
		List<JsonNode> clients = getAllClientsAsJson();
		Set<Client> ret = new HashSet<Client>();

		for (JsonNode node : clients) {
			ret.add(JsonNodeToClientAdapter.toClient(node));
		}

		return ret;
	}


	private List<JsonNode> getAllClientsAsJson() {
		return dbAccessor.executeDocumentQuery(
				getAllClientsView.getIdPath(),
				getAllClientsView.getViewName(),
				null);
	}

}
