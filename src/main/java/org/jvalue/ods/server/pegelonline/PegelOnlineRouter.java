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

import org.jvalue.ods.data.GenericValue;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.grabber.JsonGrabber;
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

		// gets the data of a single station
		Restlet singleStationRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				List<JsonNode> nodes = null;
				dbAccessor.connect();

				String name = (String) request.getAttributes().get("station");
				name = name.toUpperCase();

				try {
					nodes = dbAccessor.executeDocumentQuery(
							"_design/pegelonline", "getSingleStation", name);

					response.setEntity(nodes.get(0).toString(),
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

				List<JsonNode> nodes = null;
				dbAccessor.connect();

				String name = (String) request.getAttributes().get("station");
				name = name.toUpperCase();

				try {
					nodes = dbAccessor.executeDocumentQuery(
							"_design/pegelonline", "getMeasurements", name);

					response.setEntity(nodes.get(0).toString(),
							MediaType.APPLICATION_JSON);
				} catch (DbException ex) {
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

				try {
					nodes = dbAccessor.executeDocumentQuery(
							"_design/pegelonline", "getSingleStation", name);

					GenericValue gv = null;
					ObjectMapper mapper = new ObjectMapper();
					if (nodes.get(0).isObject()) {

						HashMap<String, Object> station;

						try {
							station = mapper
									.readValue(
											nodes.get(0).toString(),
											new TypeReference<HashMap<String, Object>>() {
											});
							double longitude = (double) station
									.get("longitude");
							double latitude = (double) station.get("latitude");

							JsonGrabber g = new JsonGrabber();
							String source = "http://overpass.osm.rambler.ru/cgi/interpreter?data=[out:json];node%28"
									+ (latitude - 0.05)
									+ "%2C"
									+ (longitude - 0.05)
									+ "%2C"
									+ (latitude + 0.05)
									+ "%2C"
									+ (longitude + 0.05) + "%29%3Bout%3B";
							gv = g.grab(source);
							String ovRet = mapper.writeValueAsString(gv);

							HashMap<String, Object> results = mapper
									.readValue(
											ovRet,
											new TypeReference<HashMap<String, Object>>() {
											});

							List<?> elements = (List<?>) results
									.get("elements");

							StringBuffer sb = new StringBuffer();
							for (Object element : elements) {
								String elementString = mapper
										.writeValueAsString(element);
								if (elementString.contains("tourism")) {
									sb.append(elementString);
								}
							}
							String message = sb.toString();
							if (!message.isEmpty()) {
								response.setEntity(sb.toString(),
										MediaType.APPLICATION_JSON);
							} else {
								response.setEntity(
										"Could not find a point of interest near: "
												+ (String) request
														.getAttributes().get(
																"station"),
										MediaType.APPLICATION_JSON);
							}

						} catch (IOException e) {
							String errorMessage = "Error during client request: "
									+ e;
							Logging.error(this.getClass(), errorMessage);
							System.err.println(errorMessage);
							response.setEntity("Internal error.",
									MediaType.TEXT_PLAIN);
						}

					}

				} catch (DbException e) {
					response.setEntity("Station not found.",
							MediaType.TEXT_PLAIN);
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
		routes.put("/pegelonline/stations/{station}", singleStationRestlet);
		routes.put("/pegelonline/stations/{station}/measurements",
				measurementsRestlet);
		routes.put("/pegelonline/stations/{station}/poi", poiRestlet);
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
