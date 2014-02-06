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
package org.jvalue.ods.server.pegelonline;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.main.DataGrabberMain;
import org.jvalue.ods.main.Router;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class PegelOnlineRouter. defines routes that start with /pegelonline/
 * 
 */
public class PegelOnlineRouter implements Router {

	/** The routes. */
	private HashMap<String, Restlet> routes;

	/** The db accessor. */
	private DbAccessor dbAccessor;

	/**
	 * Instantiates a new pegel online router.
	 * 
	 * @param dbAccessor
	 *            the db accessor
	 */
	public PegelOnlineRouter(DbAccessor dbAccessor) {
		this.dbAccessor = dbAccessor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.adapter.RouterInterface#getRoutes()
	 */
	@Override
	public Map<String, Restlet> getRoutes() {
		routes = new HashMap<String, Restlet>();

		// gets data from all stations
		Restlet stationsRestlet = new Restlet() {

			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";
				try {

					List<?> nodes = null;
					ObjectMapper mapper = new ObjectMapper();

					try {
						dbAccessor.connect();

						nodes = dbAccessor.getAllDocuments();

						message += mapper.writeValueAsString(nodes);
					} catch (RuntimeException e) {
						String errorMessage = "Could not retrieve data from db: "
								+ e;
						Logging.error(this.getClass(), errorMessage);
						System.err.println(errorMessage);
						message += mapper
								.writeValueAsString("Could not retrieve data. Try to update database via /pegelonline/update.");
					}

				} catch (IOException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					System.err.println(errorMessage);
				}

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}

		};

		// gets the data of a single station
		Restlet singleStationRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				JsonNode node = null;
				dbAccessor.connect();

				String name = (String) request.getAttributes().get("station");
				name = name.toUpperCase();

				try {
					node = (JsonNode) dbAccessor.executeDocumentQuery(
							"_design/pegelonline", "getSingleStation", name);

					response.setEntity(node.toString(),
							MediaType.APPLICATION_JSON);
				} catch (DbException ex) {
					response.setEntity("Station not found.",
							MediaType.TEXT_PLAIN);
				}
			}
		};

		// gets the current measurements of a station including its current
		// value
		Restlet measurementsRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				JsonNode node = null;
				dbAccessor.connect();

				String name = (String) request.getAttributes().get("station");
				name = name.toUpperCase();

				try {
					node = (JsonNode) dbAccessor.executeDocumentQuery(
							"_design/pegelonline", "getMeasurements", name);

					response.setEntity(node.toString(),
							MediaType.APPLICATION_JSON);
				} catch (DbException ex) {
					response.setEntity("Station not found.",
							MediaType.TEXT_PLAIN);
				}
			}
		};

		Restlet metadataRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				JsonNode node = null;
				dbAccessor.connect();

				try {
					node = (JsonNode) dbAccessor.executeDocumentQuery(
							"_design/pegelonline", "getMetadata", null);

					response.setEntity(node.toString(),
							MediaType.APPLICATION_JSON);
				} catch (DbException ex) {
					response.setEntity("Station not found.",
							MediaType.TEXT_PLAIN);
				}
			}
		};

		// updates the pegelonline data or creates the initial document if
		// necessary
		Restlet updateRestlet = new Restlet() {

			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";

				try {

					DataGrabberMain.insertPegelOnlineStationsIntoDatabase();

					message += "Database successfully updated.";
				} catch (IOException e) {
					System.err.println("Error during client request: " + e);
					message += "Could not update database: " + e.getMessage();
				}

				response.setEntity(message, MediaType.TEXT_PLAIN);

			}
		};

		routes.put("/pegelonline/stations", stationsRestlet);
		routes.put("/pegelonline/stations/{station}", singleStationRestlet);
		routes.put("/pegelonline/stations/{station}/measurements",
				measurementsRestlet);
		routes.put("/pegelonline/metadata", metadataRestlet);
		routes.put("/pegelonline/update", updateRestlet);

		return routes;
	}

}
