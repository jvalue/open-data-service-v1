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

import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.notifications.clients.HttpClient;
import org.jvalue.ods.utils.Assert;

import com.fasterxml.jackson.databind.JsonNode;


final class JsonNodeToClientAdapter implements AdapterKeys {


	public static Client toClient(JsonNode json) {
		Assert.assertNotNull(json);

		String id = json.get(KEY_ID).asText();
		String source = json.get(KEY_SOURCE).asText();
		String className = json.get(KEY_CLASS).asText();
		Assert.assertNotNull(className, source, id);

		if (className.equals(GcmClient.class.getName())) return toGcmClient(id, source);
		if (className.equals(HttpClient.class.getName())) return toRestClient(json, id, source);
		throw new IllegalArgumentException("Unkown class " + className);
	}


	private static Client toGcmClient(String id, String source) {
		return new GcmClient(id, source);
	}


	private static Client toRestClient(JsonNode json, String id, String source) {
		String restUrl = json.get(KEY_REST_URL).asText();
		String sourceParam = json.get(KEY_REST_PARAM).asText();
		boolean sendData = json.get(KEY_REST_SEND_DATA).asBoolean();
		return new HttpClient(id, source, restUrl, sourceParam, sendData);
	}



	private JsonNodeToClientAdapter() { }

}
