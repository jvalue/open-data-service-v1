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
import java.util.Map;

import org.jvalue.ods.notifications.Client;
import org.jvalue.ods.notifications.ClientVisitor;
import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.notifications.clients.HttpClient;
import org.jvalue.ods.utils.Assert;


final class ClientToMapAdapter implements ClientVisitor<Void, Map<String,Object>>, AdapterKeys {


	@Override
	public Map<String, Object> visit(GcmClient client, Void param) {
		return visitBasic(client);
	}


	@Override
	public Map<String, Object> visit(HttpClient client, Void param) {
		Map<String, Object> values = visitBasic(client);
		values.put(KEY_REST_URL, client.getRestUrl());
		values.put(KEY_REST_PARAM, client.getSourceParam());
		return values;
	}


	private Map<String, Object> visitBasic(Client client) {
		Assert.assertNotNull(client);
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put(KEY_SOURCE, client.getSource());
		ret.put(KEY_ID, client.getId());
		ret.put(KEY_CLASS, client.getClass().getName());
		return ret;
	}

}
