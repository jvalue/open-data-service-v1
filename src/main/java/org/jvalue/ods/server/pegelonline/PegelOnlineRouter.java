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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jvalue.ods.data.GenericValue;
import org.jvalue.ods.data.osm.OdsNode;
import org.jvalue.ods.data.osm.OsmData;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.grabber.OsmGrabber;
import org.jvalue.ods.grabber.XmlGrabber;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.main.Router;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class PegelOnlineRouter. defines routes that start with /pegelonline/
 * 
 */
public class PegelOnlineRouter implements Router<Restlet> {

	/** The routes. */
	private HashMap<String, Restlet> routes;

	/** The db accessor. */
	private DbAccessor<JsonNode> dbAccessor;

	/**
	 * Instantiates a new pegel online router.
	 * 
	 */
	public PegelOnlineRouter() {
		this.dbAccessor = DbFactory.createDbAccessor("pegelonline");
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

		Restlet stationsFlatRestlet = new Restlet() {

			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";
				try {

					List<JsonNode> nodes = null;
					ObjectMapper mapper = new ObjectMapper();

					try {
						dbAccessor.connect();

						nodes = dbAccessor.executeDocumentQuery("_design/pegelonline",
								"getAllStationsFlat", null);

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

				List<JsonNode> nodes = null;
				dbAccessor.connect();

				String name = (String) request.getAttributes().get("station");
				name = name.toUpperCase();

				nodes = dbAccessor.executeDocumentQuery("_design/pegelonline",
						"getSingleStation", name);

				if (!nodes.isEmpty()) {
					response.setEntity(nodes.get(0).toString(),
							MediaType.APPLICATION_JSON);
				} else {
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

				List<JsonNode> nodes = null;
				dbAccessor.connect();

				String name = (String) request.getAttributes().get("station");
				name = name.toUpperCase();

				nodes = dbAccessor.executeDocumentQuery("_design/pegelonline",
						"getMeasurements", name);

				if (!nodes.isEmpty()) {
					response.setEntity(nodes.get(0).toString(),
							MediaType.APPLICATION_JSON);
				} else {
					response.setEntity("Station not found.",
							MediaType.TEXT_PLAIN);
				}
			}
		};

		Restlet poiRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				List<JsonNode> nodes = null;
				dbAccessor.connect();

				String name = (String) request.getAttributes().get("station");
				name = name.toUpperCase();

				ObjectMapper mapper = new ObjectMapper();
				nodes = dbAccessor.executeDocumentQuery("_design/pegelonline",
						"getSingleStation", name);

				if (nodes.isEmpty()) {
					response.setEntity("Station not found.",
							MediaType.TEXT_PLAIN);
					return;
				}

				List<JsonNode> poiList = dbAccessor.executeDocumentQuery(
						"_design/pegelonline", "getPoiByStation", name);

				try {
					if (poiList.isEmpty()) {

						if (nodes.get(0).isObject()) {

							HashMap<String, Object> station;

							station = mapper
									.readValue(
											nodes.get(0).toString(),
											new TypeReference<HashMap<String, Object>>() {
											});
							double longitude = (double) station
									.get("longitude");
							double latitude = (double) station.get("latitude");

							OsmGrabber g = new OsmGrabber();
							String source = "http://api.openstreetmap.org/api/0.6/map?bbox="
									+ (longitude - 0.04)
									+ ","
									+ (latitude - 0.04)
									+ ","
									+ (longitude + 0.04)
									+ ","
									+ (latitude + 0.04);
							OsmData data = g.grab(source);

							String message = "";

							if (data != null) {

								List<OdsNode> doc = new LinkedList<OdsNode>();

								for (OdsNode n : data.getNodes()) {
									for (Entry<String, String> e : n.getTags()
											.entrySet()) {
										if (e.getKey().equals("tourism")) {
											doc.add(n);
										}
									}
								}

								message = mapper.writeValueAsString(doc);
								station.put("poi", doc);
								dbAccessor.update(station);
							}

							if (!message.isEmpty()) {
								response.setEntity(message,
										MediaType.APPLICATION_JSON);
							} else {
								response.setEntity(
										"Could not find a point of interest near: "
												+ (String) request
														.getAttributes().get(
																"station"),
										MediaType.APPLICATION_JSON);
							}

						}

					} else {
						String message = mapper.writeValueAsString(poiList
								.get(0));
						response.setEntity(message, MediaType.APPLICATION_JSON);

					}

				} catch (IOException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					System.err.println(errorMessage);
					response.setEntity("Internal error.", MediaType.TEXT_PLAIN);
				}
			}
		};

		final Restlet routeRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				dbAccessor.connect();

				ObjectMapper mapper = new ObjectMapper();

				String startStation = (String) request.getAttributes().get(
						"start");
				startStation = startStation.toUpperCase();

				String endStation = (String) request.getAttributes().get("end");
				endStation = endStation.toUpperCase();

				List<JsonNode> startNodes = dbAccessor
						.executeDocumentQuery("_design/pegelonline",
								"getSingleStation", startStation);

				List<JsonNode> endNodes = dbAccessor.executeDocumentQuery(
						"_design/pegelonline", "getSingleStation", endStation);

				if (startNodes.isEmpty() || endNodes.isEmpty()) {
					response.setEntity(
							"Could not find a route between "
									+ (String) request.getAttributes().get(
											"start")
									+ " and "
									+ (String) request.getAttributes().get(
											"end"), MediaType.APPLICATION_JSON);
					return;
				}

				try {

					double startLongitude = 0;
					double startLatitude = 0;
					double endLongitude = 0;
					double endLatitude = 0;

					if (startNodes.get(0).isObject()) {
						HashMap<String, Object> startStationMap;
						startStationMap = mapper.readValue(startNodes.get(0)
								.toString(),
								new TypeReference<HashMap<String, Object>>() {
								});
						startLongitude = (double) startStationMap
								.get("longitude");
						startLatitude = (double) startStationMap
								.get("latitude");
					}

					if (endNodes.get(0).isObject()) {
						HashMap<String, Object> endStationMap;
						endStationMap = mapper.readValue(endNodes.get(0)
								.toString(),
								new TypeReference<HashMap<String, Object>>() {
								});
						endLongitude = (double) endStationMap.get("longitude");
						endLatitude = (double) endStationMap.get("latitude");
					}

					String source = "http://www.yournavigation.org/api/1.0/gosmore.php?format=kml&flat="
							+ startLatitude
							+ "&flon="
							+ startLongitude
							+ "&tlat="
							+ endLatitude
							+ "&tlon="
							+ endLongitude
							+ "&v=motorcar&fast=1&layer=mapnik";

					XmlGrabber grabber = new XmlGrabber();
					GenericValue gv = grabber.grab(source);

					String message = mapper.writeValueAsString(gv);

					response.setEntity(message, MediaType.APPLICATION_JSON);

				} catch (IOException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					System.err.println(errorMessage);
					response.setEntity("Internal error.", MediaType.TEXT_PLAIN);
				}
			}
		};

		Restlet routeDistanceRestlet = new Restlet() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(Request request, Response response) {

				ObjectMapper mapper = new ObjectMapper();

				Response r = routeRestlet.handle(request);

				try {
					HashMap<String, Object> route = mapper.readValue(
							r.getEntityAsText(),
							new TypeReference<HashMap<String, Object>>() {
							});

					HashMap<String, Object> kml = (HashMap<String, Object>) route
							.get("kml");
					HashMap<String, Object> document = (HashMap<String, Object>) kml
							.get("Document");
					String distance = (String) document.get("distance");

					response.setEntity(distance, MediaType.TEXT_PLAIN);

				} catch (IOException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					System.err.println(errorMessage);
					response.setEntity(
							"Could not find a route between "
									+ (String) request.getAttributes().get(
											"start")
									+ " and "
									+ (String) request.getAttributes().get(
											"end"), MediaType.TEXT_PLAIN);
				}

			}
		};

		Restlet metadataRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				List<JsonNode> node = null;
				dbAccessor.connect();

				try {
					node = dbAccessor.executeDocumentQuery(
							"_design/pegelonline", "getMetadata", null);

					response.setEntity(node.get(0).toString(),
							MediaType.APPLICATION_JSON);
				} catch (DbException ex) {
					response.setEntity("Station not found.",
							MediaType.TEXT_PLAIN);
				}
			}
		};

		routes.put("/pegelonline/stations", stationsRestlet);
		routes.put("/pegelonline/stationsFlat", stationsFlatRestlet);
		routes.put("/pegelonline/stations/{station}", singleStationRestlet);
		routes.put("/pegelonline/stations/{station}/measurements",
				measurementsRestlet);
		routes.put("/pegelonline/stations/{station}/poi", poiRestlet);
		routes.put("/pegelonline/route/{start}/{end}", routeRestlet);
		routes.put("/pegelonline/routeDistance/{start}/{end}",
				routeDistanceRestlet);
		routes.put("/pegelonline/metadata", metadataRestlet);

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
