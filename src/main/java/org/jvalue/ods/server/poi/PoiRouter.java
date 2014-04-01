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
package org.jvalue.ods.server.poi;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericValue;
import org.jvalue.ods.data.generic.ListValue;
import org.jvalue.ods.data.generic.MapValue;
import org.jvalue.ods.data.generic.StringValue;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.grabber.OsmGrabber;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.main.Router;
import org.jvalue.ods.server.restlet.AccessObjectByIdRestlet;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class PoiRouter.
 */
public class PoiRouter implements Router<Restlet> {

	/** The routes. */
	private HashMap<String, Restlet> routes;

	/** The db accessor. */
	private DbAccessor<JsonNode> dbAccessor;

	/**
	 * Instantiates a new routes router.
	 */
	public PoiRouter() {
		this.dbAccessor = DbFactory.createDbAccessor("poi");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.adapter.RouterInterface#getRoutes()
	 */
	@Override
	public Map<String, Restlet> getRoutes() {
		routes = new HashMap<String, Restlet>();

		final Restlet poiRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				List<JsonNode> nodes = null;
				dbAccessor.connect();

				String name = (String) request.getAttributes().get("station");
				name = name.toUpperCase();

				DbAccessor<JsonNode> pegelOnlineDbAccessor = DbFactory
						.createDbAccessor("pegelonline");
				pegelOnlineDbAccessor.connect();
				ObjectMapper mapper = new ObjectMapper();
				nodes = pegelOnlineDbAccessor.executeDocumentQuery(
						"_design/pegelonline", "getSingleStation", name);

				if (nodes.isEmpty()) {
					response.setEntity("Station not found.",
							MediaType.TEXT_PLAIN);
					return;
				}

				List<JsonNode> poiList = dbAccessor.executeDocumentQuery(
						"_design/poi", "getPoiByStation", name);

				try {
					if (poiList.isEmpty()) {

						if (nodes.get(0).isObject()) {

							HashMap<String, Object> poi;

							poi = mapper
									.readValue(
											nodes.get(0).toString(),
											new TypeReference<HashMap<String, Object>>() {
											});
							double longitude = (double) poi.get("longitude");
							double latitude = (double) poi.get("latitude");

							OsmGrabber g = new OsmGrabber();
							String source = "http://api.openstreetmap.org/api/0.6/map?bbox="
									+ (longitude - 0.04)
									+ ","
									+ (latitude - 0.04)
									+ ","
									+ (longitude + 0.04)
									+ ","
									+ (latitude + 0.04);
							
							ListValue lv = g.grab(new DataSource(source, null, null));

							String message = "";

							if (lv != null) {				
								List<GenericValue> doc = new LinkedList<GenericValue>();
								for (GenericValue gv : lv.getList()) {									
										MapValue mv = (MapValue) gv;
										StringValue sv = (StringValue) mv.getMap().get("type");
										if (sv.getString() == "Node")
										{
											MapValue tagsMap = (MapValue) mv.getMap().get("tags");
											for(Entry<String, GenericValue> e: tagsMap.getMap().entrySet())
											{
												if (e.getKey().equals("tourism")) {
													doc.add(mv);
												}												
											}											
										}
									}

								
								message = mapper.writeValueAsString(doc);
								poi.put("poi", doc);
								dbAccessor.update(poi);
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

		Restlet idRestlet = new Restlet() {

			@Override
			public void handle(Request request, Response response) {

				String message = "";
				try {

					ObjectMapper mapper = new ObjectMapper();

					try {
						dbAccessor.connect();
						String name = (String) request.getAttributes().get(
								"station");
						name = name.toUpperCase();
						List<JsonNode> poiList = dbAccessor
								.executeDocumentQuery("_design/poi",
										"getPoiIdByStation", name);
						if (poiList.isEmpty()) {
							throw new RuntimeException();
						} else {
							message += mapper
									.writeValueAsString(poiList.get(0));
						}

					} catch (RuntimeException e) {
						String errorMessage = "Could not retrieve data from db: "
								+ e;
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

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}
		};

		routes.put("/ods/org/jvalue/konstipatrick/poi/{station}", poiRestlet);
		routes.put("/ods/org/jvalue/konstipatrick/poi/{station}/$id", idRestlet);
		routes.put("/ods/org/jvalue/konstipatrick/poi/${id}",
				new AccessObjectByIdRestlet(dbAccessor));

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
