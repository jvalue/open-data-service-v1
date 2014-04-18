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

import java.util.List;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.logger.Logging;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class DefaultRestlet.
 */
public class DefaultRestlet extends Restlet {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.restlet.Restlet#handle(org.restlet.Request,
	 * org.restlet.Response)
	 */
	@Override
	public void handle(Request request, Response response) {

		String message = "Invalid query, see /api for available queries.";

		String url = request.getResourceRef().toString();

		String[] tree = url.split("/");
		ObjectMapper mapper = new ObjectMapper();

		if (url.contains("/ods/$")) {

			DbAccessor<JsonNode> dbAccessor = DbFactory.createDbAccessor("ods");
			dbAccessor.connect();

			int pos = 0;

			// determine position where intern path begins
			for (int i = 0; i < tree.length; i++) {
				if (tree[i].startsWith("$")) {
					pos = i;
					break;
				}
			}

			try {
				JsonNode node = null;
				try {
					node = dbAccessor.getDocument(JsonNode.class,
							tree[pos].substring(1));
				} catch (RuntimeException e) {
					String errorMessage = "Could not retrieve data from db: "
							+ e;
					Logging.error(this.getClass(), errorMessage);
					System.err.println(errorMessage);
					message += mapper
							.writeValueAsString("Could not retrieve data.");
				}
				if (node != null) {
					for (int k = pos + 1; k < tree.length; k++) {
						node = node.get(tree[k]);
					}

					message = mapper.writeValueAsString(node);
				} else {
					message = "Could not find data matching the input.";
				}
			} catch (JsonProcessingException e) {
				String errorMessage = "Error during client request: " + e;
				Logging.error(this.getClass(), errorMessage);
				System.err.println(errorMessage);
			}

		} else if (url.contains("/ods/de/pegelonline/stations/")) {

			DbAccessor<JsonNode> dbAccessor = DbFactory.createDbAccessor("ods");
			dbAccessor.connect();

			int pos = 0;

			// determine position where intern path begins
			for (int i = 0; i < tree.length; i++) {
				if (tree[i].equals("stations")) {
					pos = i + 1;
					break;
				}
			}
			try {
				List<JsonNode> nodes = null;
				try {
					nodes = dbAccessor.executeDocumentQuery(
							"_design/pegelonline", "getSingleStation",
							tree[pos].toUpperCase());
				} catch (RuntimeException e) {
					String errorMessage = "Could not retrieve data from db: "
							+ e;
					Logging.error(this.getClass(), errorMessage);
					System.err.println(errorMessage);
					message += mapper
							.writeValueAsString("Could not retrieve data.");
				}
				if (nodes != null && !nodes.isEmpty()) {
					JsonNode result = nodes.get(0);
					for (int k = pos + 1; k < tree.length; k++) {
						result = result.get(tree[k]);
					}

					message = mapper.writeValueAsString(result);
				} else {
					message = "Could not find data matching the input.";
				}
			} catch (JsonProcessingException e) {
				String errorMessage = "Error during client request: " + e;
				Logging.error(this.getClass(), errorMessage);
				System.err.println(errorMessage);
			}

		}

		response.setEntity(message, MediaType.APPLICATION_JSON);
	}
}
