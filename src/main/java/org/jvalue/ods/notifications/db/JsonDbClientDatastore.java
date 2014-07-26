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

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.utils.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



final class JsonDbClientDatastore implements ClientDatastore {

	private final ObjectMapper mapper = new ObjectMapper();
	private final DbAccessor<JsonNode> dbAccessor;

	JsonDbClientDatastore(DbAccessor<JsonNode> dbAccessor) {
		Assert.assertNotNull(dbAccessor);
		this.dbAccessor = dbAccessor;
		this.dbAccessor.connect();
	}


	@Override
	public void add(Client client) {
		Assert.assertNotNull(client);

		if (!contains(client.getClientId())) {
			dbAccessor.insert(mapper.valueToTree(client));
		}
	}


	@Override
	public void remove(String clientId) {
		Assert.assertNotNull(clientId);

		for (JsonNode node : dbAccessor.getAllDocuments()) {
			try {
				if (mapper.treeToValue(node, Client.class).getClientId().equals(clientId)) {
					dbAccessor.delete(node);
				}
			} catch (JsonProcessingException jpe) {
				Logging.error(JsonDbClientDatastore.class, jpe.getMessage());
			}
		}
	}


	@Override
	public boolean contains(String clientId) {
		Assert.assertNotNull(clientId);

		for (Client client : getAll()) {
			if (client.getClientId().equals(clientId)) return true;
		}
		return false;
	}


	@Override
	public Set<Client> getAll() {
		List<JsonNode> clients = dbAccessor.getAllDocuments();
		Set<Client> ret = new HashSet<Client>();

		for (JsonNode node : clients) {
			try {
				ret.add(mapper.treeToValue(node, Client.class));
			} catch (JsonProcessingException jpe) {
				Logging.error(JsonDbClientDatastore.class, jpe.getMessage());
			}
		}

		return ret;
	}

}
