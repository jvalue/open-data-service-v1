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
package org.jvalue.ods.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.logger.Logging;
import org.restlet.Request;
import org.restlet.data.Form;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class RouterUtils.
 */
public class RouterUtils {

	/**
	 * Gets the document by attribute.
	 * 
	 * @param request
	 *            the request
	 * @param dbAccessor
	 *            the db accessor
	 * @return the document by attribute
	 */
	public String getDocumentByAttribute(Request request,
			DbAccessor<JsonNode> dbAccessor) {

		String message = "Could not find matching document.";

		Form f = request.getResourceRef().getQueryAsForm();
		String key = f.get(0).getName();
		String value = f.get(0).getValue();

		List<JsonNode> nodes = null;
		ObjectMapper mapper = new ObjectMapper();

		try {
			try {

				dbAccessor.connect();

				nodes = dbAccessor.getAllDocuments();
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

		List<HashMap<String, Object>> result = new LinkedList<>();

		try {

			for (JsonNode n : nodes) {

				if (n.isObject()) {

					HashMap<String, Object> doc;

					doc = mapper.readValue(n.toString(),
							new TypeReference<HashMap<String, Object>>() {
							});

					for (String k : doc.keySet()) {
						if (k.equals(key)) {
							String dbValue = null;
							boolean isString = false;
							try {
								dbValue = "" + doc.get(key);
								isString = true;
							} catch (Exception e) {
								isString = false;
							}
							if (dbValue != null
									&& (value.equals(dbValue))
									|| (isString && dbValue.toLowerCase()
											.equals(value))) {

								result.add(doc);

								break;

							}
						}
					}
				}

			}

			if (!result.isEmpty()) {
				message = mapper.writeValueAsString(result);
			}
		} catch (IOException e) {
			String errorMessage = "Error during client request: " + e;
			Logging.error(this.getClass(), errorMessage);
			System.err.println(errorMessage);
		}

		return message;
	}

}
