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
package org.jvalue.ods.adapter.pegelonline;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.adapter.RouterInterface;
import org.jvalue.ods.adapter.pegelonline.data.PegelOnlineData;
import org.jvalue.ods.adapter.pegelonline.data.Station;
import org.jvalue.ods.db.CouchDbAdapter;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class PegelOnlineRouter. defines routes that start with /pegelonline/
 * 
 */
public class PegelOnlineRouter implements RouterInterface {

	/** The routes. */
	private HashMap<String, Restlet> routes;

	// TODO: use database features, couchdb only serializes the pojo at the
	// moment

	/* (non-Javadoc)
	 * @see org.jvalue.ods.adapter.RouterInterface#getRoutes()
	 */
	public Map<String, Restlet> getRoutes() {
		routes = new HashMap<String, Restlet>();

		// gets data from all stations
		Restlet stationsRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";
				try {

					List<Station> sd = getListOfStations(response);

					if (sd == null) {
						return;
					}

					for (Station s : sd) {

						ObjectMapper mapper = new ObjectMapper();
						message += mapper.writeValueAsString(s);

					}

					if (!message.equals("")) {
						response.setEntity(message, MediaType.APPLICATION_JSON);
					} else {
						response.setEntity("No stations found.",
								MediaType.TEXT_PLAIN);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		};

		// gets the data of a single station
		Restlet singleStationRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";
				try {

					List<Station> sd = getListOfStations(response);

					if (sd == null) {
						return;
					}

					for (Station s : sd) {
						if (s.getLongname()
								.equalsIgnoreCase(
										(String) request.getAttributes().get(
												"station"))
								|| s.getShortname().equalsIgnoreCase(
										(String) request.getAttributes().get(
												"station"))) {

							ObjectMapper mapper = new ObjectMapper();
							message += mapper.writeValueAsString(s);

							break;
						}
					}

					if (!message.equals("")) {
						response.setEntity(message, MediaType.APPLICATION_JSON);
					} else {
						response.setEntity("Station not found.",
								MediaType.TEXT_PLAIN);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		// gets the current measurement of a station including its current
		// height value
		Restlet currentMeasurementRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";
				try {

					List<Station> sd = getListOfStations(response);

					if (sd == null) {
						return;
					}

					for (Station s : sd) {
						if (s.getLongname()
								.equalsIgnoreCase(
										(String) request.getAttributes().get(
												"station"))
								|| s.getShortname().equalsIgnoreCase(
										(String) request.getAttributes().get(
												"station"))) {

							ObjectMapper mapper = new ObjectMapper();
							message += mapper.writeValueAsString(s
									.getTimeseries().get(0)
									.getCurrentMeasurement());
							break;
						}
					}

					if (!message.equals("")) {
						response.setEntity(message, MediaType.APPLICATION_JSON);
					} else {
						response.setEntity("Station not found.",
								MediaType.TEXT_PLAIN);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		Restlet timeseriesRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";
				try {

					List<Station> sd = getListOfStations(response);

					if (sd == null) {
						return;
					}

					for (Station s : sd) {
						if (s.getLongname()
								.equalsIgnoreCase(
										(String) request.getAttributes().get(
												"station"))
								|| s.getShortname().equalsIgnoreCase(
										(String) request.getAttributes().get(
												"station"))) {

							ObjectMapper mapper = new ObjectMapper();
							message += mapper.writeValueAsString(s
									.getTimeseries());
							break;
						}
					}

					if (!message.equals("")) {
						response.setEntity(message, MediaType.APPLICATION_JSON);
					} else {
						response.setEntity("Station not found.",
								MediaType.TEXT_PLAIN);
					}

				} catch (IOException e) {
					e.printStackTrace();
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
					List<Station> list = new PegelOnlineAdapter()
							.getStationData();

					CouchDbAdapter adapter = new CouchDbAdapter("open-data-service");

					String last = adapter.getLastDocumentId();
					// if null the database is empty, ugly
					if (last != null) {

						PegelOnlineData pod = adapter.getDocument(
								PegelOnlineData.class, last);
						pod.setStations(list);
						adapter.update(pod);
					} else {
						PegelOnlineData pod = new PegelOnlineData(list);
						adapter.insert(pod);
					}

					message += "Database successfully updated.";
				} catch (IOException e) {
					e.printStackTrace();
					message += "Could not update database.";
				}

				response.setEntity(message, MediaType.TEXT_PLAIN);

			}
		};

		routes.put("/pegelonline/stations", stationsRestlet);
		routes.put("/pegelonline/stations/{station}", singleStationRestlet);
		routes.put("/pegelonline/stations/{station}/timeseries/currentMeasurement",
				currentMeasurementRestlet);
		routes.put("/pegelonline/stations/{station}/timeseries",
				timeseriesRestlet);
		routes.put("/pegelonline/update", updateRestlet);

		return routes;
	}

	// helper method to get the list of pegelonline stations
	/**
	 * Gets the list of stations.
	 *
	 * @param response the response
	 * @return the list of stations
	 */
	private List<Station> getListOfStations(Response response) {
		List<Station> sd = null;

		try {

			CouchDbAdapter adapter = new CouchDbAdapter("open-data-service");
			String docId = adapter.getLastDocumentId();
			PegelOnlineData data = adapter.getDocument(PegelOnlineData.class,
					docId);

			sd = data.getStations();

		} catch (Exception e) {

			System.err.println("Could not retrieve data.");
			response.setEntity(
					"Could not retrieve data. Try to update database via /pegelonline/update.",
					MediaType.TEXT_PLAIN);
			sd = null;
		}
		return sd;
	}

}
