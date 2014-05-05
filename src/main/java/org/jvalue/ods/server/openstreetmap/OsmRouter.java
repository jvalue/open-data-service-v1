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
import org.jvalue.ods.main.Router;
import org.jvalue.ods.server.RouterUtils;
import org.jvalue.ods.server.restlet.ClassObjectIdRestlet;
import org.jvalue.ods.server.restlet.ClassObjectRestlet;
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
public class OsmRouter implements Router<Restlet> {

	/** The routes. */
	private HashMap<String, Restlet> routes;

	/** The db accessor. */
	private DbAccessor<JsonNode> dbAccessor;

	/**
	 * Instantiates a new osm router.
	 */
	public OsmRouter() {
		this.dbAccessor = DbFactory.createDbAccessor("ods");
	}

	/**
	 * Gets the routes.
	 * 
	 * @return the routes
	 */
	@Override
	public Map<String, Restlet> getRoutes() {
		routes = new HashMap<String, Restlet>();

		Restlet getNodeByIdRestlet = new Restlet() {

			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";

				List<JsonNode> nodes = null;
				try {
					dbAccessor.connect();
					String s = (String) request.getAttributes().get("osm_id");
					nodes = dbAccessor.executeDocumentQuery("_design/osm",
							"getNodeById", s);
					ObjectMapper mapper = new ObjectMapper();
					try {
						message += mapper.writeValueAsString(nodes);
					} catch (JsonProcessingException e) {
						Logging.error(this.getClass(), e.getMessage());
						message += "Internal error";
					}

				} catch (DbException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					message += "Could not find node with ID: "
							+ (String) request.getAttributes().get("osm_id");
				}

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}

		};

		Restlet getWayByIdRestlet = new Restlet() {

			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";

				List<JsonNode> nodes = null;
				try {
					dbAccessor.connect();
					nodes = dbAccessor.executeDocumentQuery("_design/osm",
							"getWayById",
							(String) request.getAttributes().get("osm_id"));
					ObjectMapper mapper = new ObjectMapper();
					try {
						message += mapper.writeValueAsString(nodes);
					} catch (JsonProcessingException e) {
						Logging.error(this.getClass(), e.getMessage());
						message += "Internal error";
					}

				} catch (DbException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					message += "Could not find way with ID: "
							+ (String) request.getAttributes().get("osm_id");
				}

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}

		};

		Restlet getRelationByIdRestlet = new Restlet() {

			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";

				List<JsonNode> ret = null;
				try {
					dbAccessor.connect();
					ret = dbAccessor.executeDocumentQuery("_design/osm",
							"getRelationById", (String) request.getAttributes()
									.get("osm_id"));
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
							+ (String) request.getAttributes().get("osm_id");
				}

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}

		};

		Restlet getOsmDataByIdRestlet = new Restlet() {

			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";

				List<JsonNode> ret = null;
				try {
					dbAccessor.connect();
					ret = dbAccessor.executeDocumentQuery("_design/osm",
							"getOsmDataById", (String) request.getAttributes()
									.get("osm_id"));
					ObjectMapper mapper = new ObjectMapper();
					try {
						if (!ret.isEmpty()) {
							message += mapper.writeValueAsString(ret);
						} else {
							message += "Could not retrieve data.";

						}
					} catch (JsonProcessingException e) {
						Logging.error(this.getClass(), e.getMessage());
						message += "Internal error";
					}

				} catch (DbException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					message += "Could not find osm data with ID: "
							+ (String) request.getAttributes().get("osm_id");
				}

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}

		};

		Restlet getDocumentsByKeywordRestlet = new Restlet() {

			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";

				List<JsonNode> ret = null;
				try {
					dbAccessor.connect();
					ret = dbAccessor.executeDocumentQuery("_design/osm",
							"getDocumentsByKeyword", (String) request
									.getAttributes().get("keyword"));
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

		Restlet metadataRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				List<JsonNode> node = null;
				dbAccessor.connect();

				try {
					node = dbAccessor.executeDocumentQuery("_design/osm",
							"getMetadata", null);

					response.setEntity(node.get(0).toString(),
							MediaType.APPLICATION_JSON);
				} catch (DbException ex) {
					response.setEntity("Metadata not found.",
							MediaType.TEXT_PLAIN);
				}
			}
		};

		Restlet osmRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				String message = "Please specify an argument.";

				// there is an attribute in url
				if (request.getResourceRef().getQueryAsForm().size() == 1) {

					message = new RouterUtils().getDocumentByAttribute(request,
							dbAccessor);

				}

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}
		};

		routes.put("/ods/de/osm/nodes/{osm_id}", getNodeByIdRestlet);
		routes.put("/ods/de/osm/ways/{osm_id}", getWayByIdRestlet);
		routes.put("/ods/de/osm/relations/{osm_id}", getRelationByIdRestlet);
		routes.put("/ods/de/osm/data/{osm_id}", getOsmDataByIdRestlet);
		routes.put("/ods/de/osm/keyword/{osm_keyword}",
				getDocumentsByKeywordRestlet);
		routes.put("/ods/de/osm/metadata", metadataRestlet);
		routes.put("/ods/de/osm", osmRestlet);
		routes.put("/ods/de/osm/$class", new ClassObjectRestlet(dbAccessor,
				"_design/osm", "getClassObject"));
		routes.put("/ods/de/osm/$class_id", new ClassObjectIdRestlet(
				dbAccessor, "_design/osm", "getClassObjectId"));

		return routes;
	}

	/**
	 * Gets the db accessor.
	 * 
	 * @return the db accessor
	 */
	public DbAccessor<JsonNode> getDbAccessor() {
		return dbAccessor;
	}

	/**
	 * Sets the db accessor.
	 * 
	 * @param dbAccessor
	 *            the new db accessor
	 */
	public void setDbAccessor(DbAccessor<JsonNode> dbAccessor) {
		this.dbAccessor = dbAccessor;
	}

}
