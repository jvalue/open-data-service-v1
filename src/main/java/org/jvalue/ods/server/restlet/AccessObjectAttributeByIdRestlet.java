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
package org.jvalue.ods.server.restlet;

import java.io.IOException;
import java.util.HashMap;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.logger.Logging;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class AccessObjectAttributeByIdRestlet.
 */
public class AccessObjectAttributeByIdRestlet extends Restlet {
	/** The db accessor. */
	private DbAccessor<JsonNode> dbAccessor;

	/**
	 * Instantiates a new class object id restlet.
	 * 
	 * @param dbAccessor
	 *            the db accessor
	 */
	public AccessObjectAttributeByIdRestlet(DbAccessor<JsonNode> dbAccessor) {
		this.dbAccessor = dbAccessor;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.restlet.Restlet#handle(org.restlet.Request,
	 * org.restlet.Response)
	 */
	@Override
	public void handle(Request request, Response response) {

		String message = "";
		try {

			ObjectMapper mapper = new ObjectMapper();

			try {
				dbAccessor.connect();
				String id = (String) request.getAttributes().get("id");
				String attribute = (String) request.getAttributes().get(
						"attribute");

				JsonNode n = dbAccessor.getDocument(JsonNode.class, id);

				if (n == null || !n.isObject()) {
					throw new RuntimeException();
				} else {

					HashMap<String, Object> doc;

					doc = mapper.readValue(n.toString(),
							new TypeReference<HashMap<String, Object>>() {
							});

					Object ret = doc.get(attribute);

					// apart from describing external data, class objects
					// contain
					// additional metadata like their internal ods-class-name
					// (e.g. "Station"), describing these attributes' types
					// would only be
					// confusing, so it's deleted here (type: null is a marker
					// for these attributes)
					if (ret instanceof HashMap<?, ?>) {
						HashMap<?, ?> ret_map = (HashMap<?, ?>) ret;
						HashMap<String, String> value = new HashMap<>();
						value.put("type", "null");
						if (ret_map.size() == 1 && ret_map.containsValue(value)) {
							for (Object key : ret_map.keySet()) {
								ret = key;
							}
						}
					}

					if (ret != null) {
						message = mapper.writeValueAsString(ret);
					} else {
						message = "No such attribute: " + attribute;
					}
				}

			} catch (RuntimeException e) {
				String errorMessage = "Could not retrieve data from db: " + e;
				Logging.error(this.getClass(), errorMessage);
				System.err.println(errorMessage);
				message += mapper
						.writeValueAsString("Could not retrieve data.");
			}

		} catch (IOException e) {
			String errorMessage = "Error during client request: " + e;
			Logging.error(this.getClass(), errorMessage);
			System.err.println(errorMessage);
		}

		response.setEntity(message, MediaType.APPLICATION_JSON);

	}
}
