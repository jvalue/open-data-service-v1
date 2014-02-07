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
package org.jvalue.ods.server.openstreetmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.main.DataGrabberMain;
import org.jvalue.ods.main.Router;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class OsmRouter.
 */
public class OsmRouter implements Router {

	/** The routes. */
	private HashMap<String, Restlet> routes;

	/**
	 * Gets the routes.
	 * 
	 * @return the routes
	 */
	@Override
	public Map<String, Restlet> getRoutes() {
		routes = new HashMap<String, Restlet>();

		Restlet getNodeByIdRestlet = new Restlet() {

			@SuppressWarnings("unchecked")
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";

				List<JsonNode> ret = null;
				try {
					DbAccessor accessor = DbFactory
							.createCouchDbAccessor("osm");
					accessor.connect();
					ret = (List<JsonNode>) accessor.executeDocumentQuery(
							"_design/osm", "getNodeById", (String) request
									.getAttributes().get("id"));
					ObjectMapper mapper = new ObjectMapper();
					try {
						message += mapper.writeValueAsString(ret);
					} catch (JsonProcessingException e) {
						Logging.error(this.getClass(), e.getMessage());
						message += "Internal error";
					}

				} catch (DbException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					message += "Could not find node with ID: "
							+ (String) request.getAttributes().get("id");
				}

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}

		};

		Restlet getWayByIdRestlet = new Restlet() {

			@SuppressWarnings("unchecked")
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";

				List<JsonNode> ret = null;
				try {
					DbAccessor accessor = DbFactory
							.createCouchDbAccessor("osm");
					accessor.connect();
					ret = (List<JsonNode>) accessor.executeDocumentQuery(
							"_design/osm", "getWayById", (String) request
									.getAttributes().get("id"));
					ObjectMapper mapper = new ObjectMapper();
					try {
						message += mapper.writeValueAsString(ret);
					} catch (JsonProcessingException e) {
						Logging.error(this.getClass(), e.getMessage());
						message += "Internal error";
					}

				} catch (DbException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					message += "Could not find way with ID: "
							+ (String) request.getAttributes().get("id");
				}

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}

		};

		Restlet getRelationByIdRestlet = new Restlet() {

			@SuppressWarnings("unchecked")
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";

				List<JsonNode> ret = null;
				try {
					DbAccessor accessor = DbFactory
							.createCouchDbAccessor("osm");
					accessor.connect();
					ret = (List<JsonNode>) accessor.executeDocumentQuery(
							"_design/osm", "getRelationById", (String) request
									.getAttributes().get("id"));
					ObjectMapper mapper = new ObjectMapper();
					try {
						message += mapper.writeValueAsString(ret);
					} catch (JsonProcessingException e) {
						Logging.error(this.getClass(), e.getMessage());
						message += "Internal error";
					}

				} catch (DbException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					message += "Could not find relation with ID: "
							+ (String) request.getAttributes().get("id");
				}

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}

		};

		Restlet getDocumentsByKeywordRestlet = new Restlet() {

			@SuppressWarnings("unchecked")
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";

				List<JsonNode> ret = null;
				try {
					DbAccessor accessor = DbFactory
							.createCouchDbAccessor("osm");
					accessor.connect();
					ret = (List<JsonNode>) accessor.executeDocumentQuery(
							"_design/osm", "getDocumentsByKeyword",
							(String) request.getAttributes().get("keyword"));
					ObjectMapper mapper = new ObjectMapper();

					try {
						message += mapper.writeValueAsString(ret);
					} catch (JsonProcessingException e) {
						Logging.error(this.getClass(), e.getMessage());
						message += "Internal error";
					}

				} catch (DbException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					message += "Could not find documents for keyword: "
							+ (String) request.getAttributes().get("keyword");
				}

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}

		};

		Restlet updateRestlet = new Restlet() {

			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";

				DataGrabberMain.insertOsmFilesIntoDatabase();

				message += "Database successfully updated.";

				response.setEntity(message, MediaType.TEXT_PLAIN);

			}
		};

		routes.put("/osm/nodes/{id}", getNodeByIdRestlet);
		routes.put("/osm/ways/{id}", getWayByIdRestlet);
		routes.put("/osm/relations/{id}", getRelationByIdRestlet);
		routes.put("/osm/keyword/{keyword}", getDocumentsByKeywordRestlet);
		routes.put("/osm/update", updateRestlet);

		return routes;
	}
}
