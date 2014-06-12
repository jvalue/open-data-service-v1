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
package org.jvalue.ods.server.routes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.main.Router;
import org.jvalue.ods.translator.XmlTranslator;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class RoutesRouter.
 */
public class RoutesRouter implements Router<Restlet> {

	/** The routes. */
	private HashMap<String, Restlet> routes;

	/** The db accessor. */
	private DbAccessor<JsonNode> dbAccessor;

	/**
	 * Instantiates a new routes router.
	 */
	public RoutesRouter() {
		this.dbAccessor = DbFactory.createDbAccessor("ods");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.adapter.RouterInterface#getRoutes()
	 */
	@Override
	public Map<String, Restlet> getRoutes() {
		routes = new HashMap<String, Restlet>();

		final Restlet routeRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				dbAccessor.connect();

				ObjectMapper mapper = new ObjectMapper();

				Form f = request.getResourceRef().getQueryAsForm();
				String startStation = f.getFirstValue("start");
				String endStation = f.getFirstValue("end");

				if (startStation == null || endStation == null) {
					response.setEntity(
							"You have to specify start + end parameters, for example: .../route?start=rethem&end=eitze",
							MediaType.TEXT_PLAIN);
					return;
				}

				startStation = startStation.toUpperCase();
				endStation = endStation.toUpperCase();

				DbAccessor<JsonNode> pegelOnlineDbAccessor = DbFactory
						.createDbAccessor("ods");
				pegelOnlineDbAccessor.connect();
				List<JsonNode> startNodes = pegelOnlineDbAccessor
						.executeDocumentQuery("_design/pegelonline",
								"getSingleStation", startStation);

				List<JsonNode> endNodes = pegelOnlineDbAccessor
						.executeDocumentQuery("_design/pegelonline",
								"getSingleStation", endStation);

				if (startNodes.isEmpty() || endNodes.isEmpty()) {
					response.setEntity("Could not find a route between "
							+ startStation + " and " + endStation,
							MediaType.APPLICATION_JSON);
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

					DataSource ds = new DataSource(source, null, null);

					XmlTranslator grabber = new XmlTranslator();
					GenericEntity gv = grabber.translate(ds);

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

					if (r.getEntity().getMediaType() == MediaType.TEXT_PLAIN) {
						throw new IOException(r.getEntityAsText());
					}

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
					response.setEntity(r.getEntity());
				}

			}
		};

		routes.put("/services/org/jvalue/routes/route", routeRestlet);
		routes.put("/services/org/jvalue/routes/routeDistance",
				routeDistanceRestlet);

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
