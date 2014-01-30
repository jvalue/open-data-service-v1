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
import org.jvalue.ods.server.Router;
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

					List<JsonNode> nodes = null;
					try {
						nodes = getListOfStations(response);
					} catch (RuntimeException e) {
						return;
					}

					ObjectMapper mapper = new ObjectMapper();
					message += mapper.writeValueAsString(nodes);

				} catch (IOException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					System.err.println(errorMessage);
				}

				if (!message.equals("")) {
					response.setEntity(message, MediaType.APPLICATION_JSON);
				} else {
					response.setEntity("No stations found.",
							MediaType.TEXT_PLAIN);
				}
			}

		};

		// gets the data of a single station
		Restlet singleStationRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				JsonNode node = null;
				dbAccessor.connect();

				String s = (String) request.getAttributes().get("station");
				s = s.toUpperCase();

				try {
					node = (JsonNode) dbAccessor.getNodeByName(s);

					response.setEntity(node.toString(),
							MediaType.APPLICATION_JSON);
				} catch (DbException ex) {
					response.setEntity("Station not found.",
							MediaType.TEXT_PLAIN);
				}
			}
		};

		// gets the current measurement of a station including its current
		// height value
		// Restlet currentMeasurementRestlet = new Restlet() {
		// @Override
		// public void handle(Request request, Response response) {
		// // Print the requested URI path
		// String message = "";
		// try {
		// List<Station> sd = null;
		// try {
		// sd = getListOfStations(response);
		// } catch (RuntimeException e) {
		// String errorMessage = "Runtime exception occured: " + e;
		// Logging.error(this.getClass(), errorMessage);
		// return;
		// }
		//
		// for (Station s : sd) {
		// if (s.getLongname()
		// .equalsIgnoreCase(
		// (String) request.getAttributes().get(
		// "station"))
		// || s.getShortname().equalsIgnoreCase(
		// (String) request.getAttributes().get(
		// "station"))) {
		//
		// ObjectMapper mapper = new ObjectMapper();
		// message += mapper.writeValueAsString(s
		// .getTimeseries().get(0)
		// .getCurrentMeasurement());
		// break;
		// }
		// }
		//
		// } catch (IOException e) {
		// String errorMessage = "Error during client request: " + e;
		// Logging.error(this.getClass(), errorMessage);
		// System.err.println(errorMessage);
		// }
		// if (!message.equals("")) {
		// response.setEntity(message, MediaType.APPLICATION_JSON);
		// } else {
		// response.setEntity("Station not found.",
		// MediaType.TEXT_PLAIN);
		// }
		// }
		// };

		// Restlet timeseriesRestlet = new Restlet() {
		// @Override
		// public void handle(Request request, Response response) {
		// // Print the requested URI path
		// String message = "";
		// try {
		//
		// List<Station> sd = null;
		//
		// try {
		// sd = getListOfStations(response);
		// } catch (RuntimeException e) {
		// String errorMessage = "Runtime exception occured: " + e;
		// Logging.error(this.getClass(), errorMessage);
		// return;
		// }
		//
		// for (Station s : sd) {
		// if (s.getLongname()
		// .equalsIgnoreCase(
		// (String) request.getAttributes().get(
		// "station"))
		// || s.getShortname().equalsIgnoreCase(
		// (String) request.getAttributes().get(
		// "station"))) {
		//
		// ObjectMapper mapper = new ObjectMapper();
		// message += mapper.writeValueAsString(s
		// .getTimeseries());
		// break;
		// }
		// }
		//
		// } catch (IOException e) {
		// String errorMessage = "Error during client request: " + e;
		// Logging.error(this.getClass(), errorMessage);
		// System.err.println("Error during client request: " + e);
		// }
		//
		// if (!message.equals("")) {
		// response.setEntity(message, MediaType.APPLICATION_JSON);
		// } else {
		// response.setEntity("Station not found.",
		// MediaType.TEXT_PLAIN);
		// }
		// }
		// };

		// updates the pegelonline data or creates the initial document if
		// necessary
		Restlet updateRestlet = new Restlet() {

			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";

				try {

					DataGrabberMain.main(new String[0]);

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
		// routes.put(
		// "/pegelonline/stations/{station}/timeseries/currentMeasurement",
		// currentMeasurementRestlet);
		// routes.put("/pegelonline/stations/{station}/timeseries",
		// timeseriesRestlet);
		routes.put("/pegelonline/update", updateRestlet);

		return routes;
	}

	// helper method to get the list of pegelonline stations
	/**
	 * Gets the list of stations from db.
	 * 
	 * @param response
	 *            the response
	 * @return the list of stations
	 * @throws RuntimeException
	 *             the runtime exception
	 */
	@SuppressWarnings("unchecked")
	private List<JsonNode> getListOfStations(Response response)
			throws RuntimeException {
		List<JsonNode> nodes;

		try {
			dbAccessor.connect();

			nodes = (List<JsonNode>) dbAccessor.getAllDocuments();

		} catch (RuntimeException e) {
			String errorMessage = "Could not retrieve data from db: " + e;
			Logging.error(this.getClass(), errorMessage);
			System.err.println(errorMessage);
			response.setEntity(
					"Could not retrieve data. Try to update database via /pegelonline/update.",
					MediaType.TEXT_PLAIN);
			throw e;
		}
		return nodes;
	}

}
