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

import org.jvalue.ods.notifications.Client;
import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.utils.Assert;

import com.fasterxml.jackson.databind.JsonNode;


final class JsonNodeToClientAdapter implements AdapterKeys {


	public static Client toClient(JsonNode json) {
		Assert.assertNotNull(json);

		String id = json.get(KEY_ID).asText();
		String source = json.get(KEY_SOURCE).asText();
		String className = json.get(KEY_CLASS).asText();
		Assert.assertNotNull(className, source, id);

		System.out.println(GcmClient.class.getName());
		System.out.println(className);
		if (className.equals(GcmClient.class.getName())) return toGcmClient(id, source);
		throw new IllegalArgumentException("Unkown class " + className);
	}


	private static Client toGcmClient(String id, String source) {
		return new GcmClient(id, source);
	}



	private JsonNodeToClientAdapter() { }

}
